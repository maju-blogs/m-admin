package org.m.pay.service;


import org.m.common.entity.po.ClientInfoPo;
import com.mybatisflex.core.service.IService;

import java.util.List;
import java.util.Set;

/**
 * 客户端表 服务层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
public interface IClientInfoService extends IService<ClientInfoPo> {

    List<ClientInfoPo> getAllClient();

    Set<String> getAllTopic();

}