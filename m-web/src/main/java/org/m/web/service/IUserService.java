package org.m.web.service;


import com.mybatisflex.core.service.IService;
import org.m.common.entity.po.UserPo;

/**
 * 用户表 服务层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
public interface IUserService extends IService<UserPo> {

    boolean updateOcr(UserPo user);
}