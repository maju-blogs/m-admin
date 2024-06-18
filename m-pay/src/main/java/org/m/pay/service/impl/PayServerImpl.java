package org.m.pay.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.m.common.constant.PublicConstant;
import org.m.common.entity.dto.TaskExecuteDto;
import org.m.common.entity.po.PayConfigPo;
import org.m.common.entity.po.PayOrderPo;
import org.m.common.enums.PayStatusEnum;
import org.m.common.enums.QrLogoEnum;
import org.m.common.entity.dto.PayDto;
import org.m.common.entity.dto.QrDto;
import org.m.common.enums.SseMsgTypeEnum;
import org.m.common.inter.ISseEmitterService;
import org.m.mqtt.starter.server.MqttServerCache;
import org.m.pay.handle.PayHandleMsgImpl;
import org.m.pay.service.IPayConfigService;
import org.m.pay.service.IPayOrderService;
import org.m.pay.service.IPayQrConfigService;
import org.m.pay.service.IPayServer;
import org.m.pay.util.NetNameUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static org.m.common.entity.po.table.PayConfigPoTableDef.PAY_CONFIG_PO;
import static org.m.common.entity.po.table.PayOrderPoTableDef.PAY_ORDER_PO;

@Service
@Slf4j
public class PayServerImpl implements IPayServer {
    Map<String, PayDto> payMap = new ConcurrentHashMap<>();
    Map<String, String> payIdMap = new ConcurrentHashMap<>();
    @Resource
    private IPayQrConfigService iPayQrConfigService;


    @Resource
    private IPayConfigService iPayConfigService;

    @Resource
    private IPayOrderService iPayOrderService;

    @Resource
    private PayHandleMsgImpl payHandleMsg;

    @Resource
    private ISseEmitterService iSseEmitterService;

    private static Map<String, String> sseMap = new HashMap<>();


    @Override
    public PayDto getPay(HttpServletRequest request) {
        String paySessionId = getSessionId(request);
        if (paySessionId == null || request.getSession().getAttribute("clientId") == null) {
            throw new RuntimeException("支付通道异常!,请刷新重试");
        }
        PayDto payDto = payMap.get(paySessionId);
        Date now = new Date();
        if (null != payDto && null != payDto.getPayTimeOutDate() && payDto.getPayTimeOutDate().before(now)) {
            //到达删除任务时间 可以重新支付
            if (payDto.getPayDelDate().before(now)) {
                payMap.remove(paySessionId);
                payDto = null;
            } else {
                //有支付超时 但是还未超过一分钟
                throw new RuntimeException("操作过于频繁，请稍后再试");
            }
        }
        if (payDto == null || StringUtils.isEmpty(payDto.getPayId())) {
            PayDto dto = PayDto.builder().payId(IdUtil.getSnowflakeNextIdStr()).payIp(paySessionId).payUserName(NetNameUtil.getNetWorkName()).build();
            payMap.put(paySessionId, dto);
            return dto;
        }

        return payDto;
    }

    @Override
    public PayDto pay(HttpServletRequest request, PayDto dto) {
        String sessionId = getSessionId(request);
        if (sessionId == null) {
            throw new RuntimeException("支付通道异常!,请刷新重试");
        }
        PayDto payDto = payMap.get(sessionId);
        try {
            if (payDto == null) {
                throw new RuntimeException("非法操作!");
            }
            Date now = new Date();
            if (null != payDto && null != payDto.getPayDelDate() && payDto.getPayDelDate().after(now)) {
                //有支付超时 但是还未超过一分钟
                throw new RuntimeException("操作过于频繁，请稍后再试");
            }
            if (dto.getPayType() == null) {
                dto.setPayType(QrLogoEnum.WX.getType());
            }
            QrDto qrDto = iPayQrConfigService.getOneUnLockQr(dto.getPayType(), dto.getPayAmount());
            PayConfigPo one = iPayConfigService.getOne(QueryWrapper.create().from(PAY_CONFIG_PO));
            if (qrDto == null || one == null) {
                log.error("未找到支付配置");
                throw new RuntimeException("支付通道异常!,请刷新重试");
            }
            ConcurrentHashMap<String, String> clientInfo = MqttServerCache.getOneClientByClientId(one.getClientId());
            if (!clientInfo.get("topic").contains(one.getPayTopic())) {
                log.error("设备不在线");
                throw new RuntimeException("支付通道异常!,请刷新重试");
            }
            payDto.setPayType(dto.getPayType());
            payDto.setPayUserName(dto.getPayUserName());
            payDto.setPayAmount(dto.getPayAmount());
            payDto.setPayDesc(dto.getPayDesc());
            payDto.setPayStatus(PayStatusEnum.PAY_ING.getType());
            payDto.setPayQrId(qrDto.getQrId());
            payDto.setPayQrBase64(qrDto.getQrBase64());
            payDto.setPayTimeOutDate(DateUtil.offset(now, DateField.SECOND, one.getPayTimeOut()));
            payDto.setPayDelDate(DateUtil.offset(now, DateField.SECOND, one.getPayTimeOut() + PublicConstant.PAY_TASK_DELAY_TIME));
            iPayOrderService.saveOrder(payDto);
            sseMap.put(payDto.getPayId(), request.getSession().getAttribute("clientId").toString());
            payIdMap.put(payDto.getPayId(), sessionId);
            //任务超时时间增加延时
            payHandleMsg.addPayTask(one, qrDto.getQrMark(), payDto.getPayId(), one.getPayTimeOut() + PublicConstant.PAY_TASK_DELAY_TIME, consumer);
            return payDto;
        } catch (Exception e) {
            payMap.remove(sessionId);
            log.error("支付异常", e);
            throw new RuntimeException("支付通道异常!,请刷新重试");
        }
    }

    @Override
    public void onCallBack(TaskExecuteDto taskExecuteDto) {
        try {
            log.info("支付结果，payDto:{}", JSONObject.toJSONString(taskExecuteDto));
            PayOrderPo build = PayOrderPo.builder().payType(taskExecuteDto.getPayType()).payTime(taskExecuteDto.getPayTime()).payStatus(taskExecuteDto.getPayStatus()).build();
            iPayOrderService.update(build, QueryWrapper.create().from(PAY_ORDER_PO).where(PAY_ORDER_PO.PAY_ID.eq(taskExecuteDto.getPayId())));
            String sseClientId = sseMap.get(taskExecuteDto.getPayId());
            iSseEmitterService.sendMessageToOneClient(sseClientId, SseMsgTypeEnum.getSSEMsg(taskExecuteDto.getPayStatus() + "", SseMsgTypeEnum.PAY_RESULT));
            iPayQrConfigService.unlockQr(taskExecuteDto.getQrMark());
            String ip = payIdMap.get(taskExecuteDto.getPayId());
            payMap.remove(ip);
        } catch (Exception e) {
            log.error("回调处理异常", e);
        }

    }

    Consumer<TaskExecuteDto> consumer = pay -> this.onCallBack(pay);


    /**
     * 获取sessionId
     *
     * @param request HttpServletRequest
     * @return 真实ip
     */
    public String getSessionId(HttpServletRequest request) {
        HttpSession object = request.getSession();
        if (null != object) {
            return object.getId();
        }
        return null;
    }

}
