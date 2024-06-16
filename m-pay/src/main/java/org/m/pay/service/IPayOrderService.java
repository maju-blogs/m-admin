package org.m.pay.service;


import org.m.common.entity.po.PayOrderPo;
import com.mybatisflex.core.service.IService;
import org.m.common.entity.dto.PayDto;

/**
 * 支付订单表 服务层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
public interface IPayOrderService extends IService<PayOrderPo> {

    void saveOrder(PayDto payDto);
}