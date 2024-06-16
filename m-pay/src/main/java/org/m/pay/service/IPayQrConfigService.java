package org.m.pay.service;


import cn.dev33.satoken.util.SaResult;
import org.m.common.entity.po.PayQrConfigPo;
import com.mybatisflex.core.service.IService;
import org.m.common.entity.dto.QrDto;

import java.math.BigDecimal;

/**
 * 支付二维码 服务层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
public interface IPayQrConfigService extends IService<PayQrConfigPo> {

    SaResult saveAndSetting(PayQrConfigPo payQrConfig);

    SaResult updateAndSettingById(PayQrConfigPo payQrConfig);

    QrDto getOneUnLockQr(Integer payType, BigDecimal payAmount);

    void lockQr(String qrMark);

    void unlockQr(String qrMark);

    void initQrCache();
}