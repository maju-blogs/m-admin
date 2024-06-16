package org.m.web.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.m.common.entity.dto.MqttUpDto;
import org.m.common.entity.dto.TaskExecuteDto;
import org.m.common.entity.po.TaskExecuteLogPo;
import org.m.common.entity.po.TaskMineListPo;
import org.m.common.enums.MqttMsgTypeEnum;
import org.m.mqtt.starter.client.MqttGateway;
import org.m.web.service.ITaskExecuteLogService;
import org.m.web.service.ITaskManager;
import org.m.web.service.ITaskMineListService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.m.common.entity.po.table.TaskMineListPoTableDef.TASK_MINE_LIST_PO;

@Service
public class TaskManagerImpl implements ITaskManager {

    @Resource
    private ITaskMineListService iTaskMineListService;

    @Resource
    private TaskMqttHandleImpl taskMqttHandleImpl;

    @Resource
    private ITaskExecuteLogService iTaskExecuteLogService;

    public TaskExecuteDto doTask(String taskId, String topic) {
        MqttUpDto build = MqttUpDto.builder().
                taskSnr(IdUtil.getSnowflakeNextIdStr())
                .type(MqttMsgTypeEnum.DO_TASK.getType())
                .data(taskId).build();
        TaskExecuteDto taskExecuteDto = taskMqttHandleImpl.doTask(build, topic);
        TaskExecuteLogPo log = TaskExecuteLogPo.builder()
                .topic(topic)
                .doParams(taskId)
                .doResult(taskExecuteDto.getResult())
                .doStatus(taskExecuteDto.isOk() ? 1 : 0).build();
        iTaskExecuteLogService.save(log);
        return taskExecuteDto;
    }

    @Override
    public Boolean downloadTask(String taskId, String topic) {
        TaskMineListPo one = iTaskMineListService.getOne(QueryWrapper.create().from(TASK_MINE_LIST_PO).where(TASK_MINE_LIST_PO.TASK_ID.eq(taskId)));
        if (one == null) {
            return false;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileHex", one.getTaskHex());
        MqttUpDto build = MqttUpDto.builder().data(jsonObject.toString())
                .taskSnr(IdUtil.getSnowflakeNextIdStr())
                .type(MqttMsgTypeEnum.ADD_TASK.getType())
                .build();
        TaskExecuteDto taskExecuteDto = taskMqttHandleImpl.doTask(build, topic);
        return taskExecuteDto.isOk();
    }
}
