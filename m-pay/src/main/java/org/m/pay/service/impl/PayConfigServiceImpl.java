package org.m.pay.service.impl;


import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.m.pay.mapper.PayConfigMapper;
import org.springframework.stereotype.Service;
import org.m.pay.service.IPayConfigService;
import org.m.common.entity.po.PayConfigPo;


/**
 * 支付设置 服务层实现。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Service
@Slf4j
public class PayConfigServiceImpl extends ServiceImpl<PayConfigMapper, PayConfigPo> implements IPayConfigService {

}