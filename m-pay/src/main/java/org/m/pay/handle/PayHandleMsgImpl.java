package org.m.pay.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.m.common.constant.PublicConstant;
import org.m.common.entity.dto.MqttUpDto;
import org.m.common.entity.dto.TaskExecuteDto;
import org.m.common.entity.po.PayConfigPo;
import org.m.common.enums.MqttMsgTypeEnum;
import org.m.common.enums.PayStatusEnum;
import org.m.mqtt.starter.client.*;
import org.m.common.entity.dto.PayDto;
import org.m.pay.service.IPayConfigService;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@DependsOn("inbound")
public class PayHandleMsgImpl extends AbsTaskMqttHandle {

    @Resource
    private MyMqttClient myMqttClient;

    @PostConstruct
    public void init() {
        initTopics();
    }

    @Resource
    private IPayConfigService iPayConfigService;

    private Boolean isRun = false;

    ConcurrentLinkedQueue<TaskExecuteDto> payQueue = new ConcurrentLinkedQueue<>();
    Set<String> payTopics = new HashSet<>();

    String dateformat = "yyyy年MM月dd日 HH:mm:ss";
    // 现在创建 matcher 对象
    String year = DateTime.now().year() + "年";

    @Override
    public Object handleMsg(String topic, String data) {
        return super.handleMsg(topic, data);
    }


    @Override
    public boolean match(String topic, String data) {
        try {
            if (payTopics.contains(topic.replace(PublicConstant.MQTT_CLIENT_OUT, ""))) {
                MqttUpDto mqttUpDto = JSONObject.parseObject(data, MqttUpDto.class);
                if (exeTask.get(mqttUpDto.getTaskSnr()) != null) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


   public boolean addPayTask(PayConfigPo configPo, String qrMark, String payId, int timeOut, Consumer<TaskExecuteDto> consumer) {
        DateTime expireTime = DateUtil.offset(new Date(), DateField.SECOND, timeOut);
        TaskExecuteDto executeDto = TaskExecuteDto.builder().qrMark(qrMark).taskId(configPo.getTaskId()).topic(configPo.getPayTopic()).taskType(MqttMsgTypeEnum.DO_TASK.getType()).consumer(consumer).payId(payId).regex(configPo.getRegex()).payType(configPo.getPayType()).payStartTime(new Date()).exeExpireTime(expireTime).build();
        payQueue.offer(executeDto);
        new Thread(() -> {
            doPay();
        }).start();
        return true;
    }

    private void doPay() {
        synchronized (isRun) {
            if (isRun) {
                return;
            }
        }
        isRun = true;
        while (isRun) {
            TaskExecuteDto taskExecuteDto = payQueue.poll();
            try {
                if (null == taskExecuteDto) {
                    isRun = false;
                    return;
                }
                MqttUpDto dto = new MqttUpDto(taskExecuteDto.getTopic(), taskExecuteDto.getTaskType(), IdUtil.getSnowflakeNextIdStr(), taskExecuteDto.getTaskId());
                TaskExecuteDto result = super.doTask(dto, taskExecuteDto.getTopic());
                log.debug("result:{}", JSON.toJSONString(result));
                if (result.isOk() && StrUtil.isNotEmpty(result.getResult()) && mather(result.getResult(), taskExecuteDto)) {
                    taskExecuteDto.setPayStatus(PayStatusEnum.PAY_SUCCESS.getType());
                    taskExecuteDto.getConsumer().accept(taskExecuteDto);
                    continue;
                }
                if (taskExecuteDto.getExeExpireTime().before(new Date())) {
                    taskExecuteDto.setOk(true);
                    taskExecuteDto.setPayStatus(PayStatusEnum.PAY_FAIL.getType());
                    taskExecuteDto.getConsumer().accept(taskExecuteDto);
                    continue;
                }
                payQueue.offer(taskExecuteDto);
                Thread.sleep(500);
            } catch (Exception e) {
                isRun = false;
                if (null != taskExecuteDto) {
                    payQueue.offer(taskExecuteDto);
                }
                log.error("获取支付状态异常", e);
            }
        }

    }



    private boolean mather(String result, TaskExecuteDto taskExecuteDto) {
        Pattern r = Pattern.compile(taskExecuteDto.getRegex());
        Matcher m = r.matcher(result);
        // 现在创建 matcher 对象
        while (m.find()) {
            DateTime parse = DateUtil.parse(year + m.group(1) + PublicConstant.MAX_SECOND, dateformat);
            long betweenSecond = DateUtil.between(parse, taskExecuteDto.getPayStartTime(), DateUnit.SECOND);
            long payTime = DateUtil.between(taskExecuteDto.getPayStartTime(), taskExecuteDto.getExeExpireTime(), DateUnit.SECOND);
            log.debug("time:{},qrMark:{},payTime:{}", parse, m.group(3),payTime);
            if ((betweenSecond < payTime) && m.group(3).equals(taskExecuteDto.getQrMark())) {
                taskExecuteDto.setPayTime(parse);
                return true;
            }
        }
        return false;
    }

    public void initTopics() {
        List<PayConfigPo> list = iPayConfigService.list();
        if (CollUtil.isEmpty(list)) {
            log.error("暂无支付配置");
            return;
        }
        payTopics.clear();
        payTopics.addAll(list.stream().map(PayConfigPo::getPayTopic).collect(Collectors.toSet()));
        payTopics.forEach(topic -> {
            if (StrUtil.isNotEmpty(topic)) {
                try {
                    myMqttClient.addTopic(this, topic);
                } catch (Exception e) {
                    log.error("订阅主题异常");
                }
            }
        });

    }
}
