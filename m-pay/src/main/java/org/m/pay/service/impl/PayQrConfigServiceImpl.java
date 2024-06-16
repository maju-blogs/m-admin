package org.m.pay.service.impl;


import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.m.common.enums.QrLogoEnum;
import org.m.common.entity.dto.QrDto;
import org.m.pay.mapper.PayQrConfigMapper;
import org.m.pay.util.QrUtil;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.m.pay.service.IPayQrConfigService;
import org.m.common.entity.po.PayQrConfigPo;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 支付二维码 服务层实现。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Service
public class PayQrConfigServiceImpl extends ServiceImpl<PayQrConfigMapper, PayQrConfigPo> implements IPayQrConfigService {

    public static ConcurrentLinkedDeque<QrDto> qrDtos = new ConcurrentLinkedDeque<>();

    @Resource
    private ResourceLoader resourceLoader;

    @Override
    public SaResult saveAndSetting(PayQrConfigPo payQrConfig) {
        String qrOldBase64 = payQrConfig.getQrOldBase64();
        QrLogoEnum qrLogoEnum = QrLogoEnum.forType(payQrConfig.getQrLogoType());
        String newBase64QrStr = QrUtil.beautificationQr(resourceLoader, qrOldBase64, qrLogoEnum.getPath());
        if (null == newBase64QrStr) {
            return SaResult.error("二维码解析失败");
        }
        payQrConfig.setQrBase64(newBase64QrStr);
        save(payQrConfig);
        initQrCache();
        return SaResult.ok();
    }

    @Override
    public SaResult updateAndSettingById(PayQrConfigPo payQrConfig) {
        String qrOldBase64 = payQrConfig.getQrOldBase64();
        QrLogoEnum qrLogoEnum = QrLogoEnum.forType(payQrConfig.getQrLogoType());
        String newBase64QrStr = QrUtil.beautificationQr(resourceLoader, qrOldBase64, qrLogoEnum.getPath());
        if (null == newBase64QrStr) {
            return SaResult.error("二维码解析失败");
        }
        payQrConfig.setQrBase64(newBase64QrStr);
        updateById(payQrConfig);
        initQrCache();
        return SaResult.ok();
    }

    @Override
    public QrDto getOneUnLockQr(Integer payType, BigDecimal payAmount) {
        long now = System.currentTimeMillis();
        synchronized (qrDtos) {
            Optional<QrDto> min = qrDtos.stream().filter(item -> (!item.isLock() || item.getTimeoutLock() < now)
                            && payType == item.getQrType()
                            && payAmount.compareTo(item.getAmount()) == 0)
                    .min(Comparator.comparing(QrDto::getLastUseTime));
            if (!min.isPresent()) {
                initQrCache();
                min = qrDtos.stream().filter(item -> (!item.isLock() || item.getTimeoutLock() < now)
                                && payType == item.getQrType()
                                && payAmount.compareTo(item.getAmount()) == 0)
                        .min(Comparator.comparing(QrDto::getLastUseTime));
            }
            if (min.isPresent()) {
                QrDto qrDto = min.get();
                lockQr(qrDto.getQrMark());
                return qrDto;
            }
        }

        return null;
    }

    @Override
    public synchronized void lockQr(String qrMark) {
        synchronized (qrDtos) {
            for (QrDto qrDto : qrDtos) {
                if (qrMark.equals(qrDto.getQrMark())) {
                    qrDto.setLock(true);
                    qrDto.setTimeoutLock(DateUtil.offset(new Date(), DateField.MINUTE, 5).getTime());
                    qrDto.setLastUseTime(System.currentTimeMillis());
                }
            }
        }

    }

    @Override
    public synchronized void unlockQr(String qrMark) {
        synchronized (qrDtos) {
            for (QrDto qrDto : qrDtos) {
                if (qrMark.equals(qrDto.getQrMark())) {
                    qrDto.setLock(false);
                }
            }
        }
    }

    @Override
    public void initQrCache() {
        List<PayQrConfigPo> list = list();
        qrDtos.clear();
        list.forEach(item -> {
            QrDto qrDto = QrDto.builder().qrId(item.getId()).amount(item.getPayAmount()).qrMark(item.getQrMark()).qrType(item.getQrType()).qrBase64(item.getQrBase64()).isLock(false).build();
            qrDtos.add(qrDto);
        });
    }
}