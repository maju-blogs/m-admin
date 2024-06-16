package org.m.web.service;


import org.m.common.entity.po.TaskMineListPo;
import com.mybatisflex.core.service.IService;

/**
 * 我的任务 服务层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
public interface ITaskMineListService extends IService<TaskMineListPo> {

    void saveOrUpdateByTaskId(TaskMineListPo taskMineList);
}