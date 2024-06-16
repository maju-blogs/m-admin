package org.m.pay.service.impl;


import org.m.common.entity.dto.PayDto;
import org.springframework.stereotype.Service;
import org.m.pay.service.IPayOrderService;
import org.m.common.entity.po.PayOrderPo;
import org.m.pay.mapper.PayOrderMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 支付订单表 服务层实现。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrderPo> implements IPayOrderService {

    @Override
    public void saveOrder(PayDto payDto) {
        PayOrderPo build = PayOrderPo.builder()
                .payId(payDto.getPayId())
                .payAmount(payDto.getPayAmount())
                .payUserName(payDto.getPayUserName())
                .payDesc(payDto.getPayDesc())
                .payStatus(payDto.getPayStatus()).build();
        save(build);
    }
}