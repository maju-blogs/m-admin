package org.m.web.service.impl;


import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.m.common.entity.po.UserPo;
import org.m.web.mapper.UserMapper;
import org.m.web.service.IUserService;
import org.springframework.stereotype.Service;

import static org.m.common.entity.po.table.UserPoTableDef.USER_PO;

/**
 * 用户表 服务层实现。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements IUserService {

    @Override
    public boolean updateOcr(UserPo user) {
        return update(user, QueryWrapper.create()
                .from(USER_PO).where("1=1"));
    }
}