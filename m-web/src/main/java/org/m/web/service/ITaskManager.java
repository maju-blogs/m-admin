package org.m.web.service;

import org.m.common.entity.dto.TaskExecuteDto;

public interface ITaskManager {
    TaskExecuteDto doTask(String taskId, String topic);

    Boolean downloadTask(String taskId, String topic);
}
