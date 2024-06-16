package org.m.web.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.m.web.service.ITaskMineListService;
import org.m.common.entity.po.TaskMineListPo;
import org.m.web.mapper.TaskMineListMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import java.util.Date;

import static org.m.common.entity.po.table.TaskMineListPoTableDef.TASK_MINE_LIST_PO;

/**
 * 我的任务 服务层实现。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Service
public class TaskMineListServiceImpl extends ServiceImpl<TaskMineListMapper, TaskMineListPo> implements ITaskMineListService {

    @Override
    public void saveOrUpdateByTaskId(TaskMineListPo taskMineList) {
        QueryWrapper where = QueryWrapper.create()
                .from(TASK_MINE_LIST_PO).where(TASK_MINE_LIST_PO.TASK_ID.eq(taskMineList.getTaskId()));
        TaskMineListPo one = getOne(where);
        if (null != one) {
            taskMineList.setUpdateTime(new Date());
            update(taskMineList, where);
        } else {
            taskMineList.setId(null);
            save(taskMineList);
        }
    }
}