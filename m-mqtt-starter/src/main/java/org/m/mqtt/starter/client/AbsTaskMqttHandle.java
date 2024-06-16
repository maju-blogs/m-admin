package org.m.mqtt.starter.client;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.m.common.constant.PublicConstant;
import org.m.common.entity.dto.MqttUpDto;
import org.m.common.entity.dto.TaskExecuteDto;
import org.m.common.enums.MqttMsgTypeEnum;

import java.util.concurrent.*;


@Slf4j
public class AbsTaskMqttHandle implements IMqttHandleMsg {


    @Resource
    private MqttGateway gateway;

    public ConcurrentMap<String, TaskExecuteDto> exeTask = new ConcurrentHashMap<>();
    public ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public boolean match(String topic, String data) {
        try {
            MqttUpDto mqttUpDto = JSONObject.parseObject(data, MqttUpDto.class);
            if (exeTask.get(mqttUpDto.getTaskSnr()) != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public Object handleMsg(String topic, String data) {
        MqttUpDto mqttUpDto = JSONObject.parseObject(data, MqttUpDto.class);
        TaskExecuteDto executeDto = (exeTask.get(mqttUpDto.getTaskSnr()));
        if (executeDto == null) {
            return null;
        }
        if (MqttMsgTypeEnum.UPLOAD_TASK.getType().equals(mqttUpDto.getType())) {
        } else if (MqttMsgTypeEnum.UPLOAD_DATA.getType().equals(mqttUpDto.getType())) {
            if (executeDto != null) {
                executeDto.setResult(mqttUpDto.getData());
            }
        } else if (MqttMsgTypeEnum.UPLOAD_RESULT.getType().equals(mqttUpDto.getType())) {
            synchronized (executeDto) {
                if ("true".equals(mqttUpDto.getData()) && executeDto != null) {
                    executeDto.setOk(true);
                } else if (StrUtil.isNotEmpty(executeDto.getResult())) {
                    executeDto.setOk(true);
                }
                executeDto.notify();
            }
        }
        return executeDto;
    }

    @Override
    public TaskExecuteDto doTask(MqttUpDto dto, String topic) {
        TaskExecuteDto executeDto = TaskExecuteDto.builder().taskSnr(dto.getTaskSnr()).build();
        exeTask.put(executeDto.getTaskSnr(), executeDto);
        CompletableFuture<TaskExecuteDto> future = CompletableFuture.supplyAsync(() -> {
            gateway.sendToMqtt(topic, JSON.toJSONString(dto));
            TaskExecuteDto taskExecuteDto = exeTask.get(dto.getTaskSnr());
            synchronized (taskExecuteDto) {
                try {
                    taskExecuteDto.wait(PublicConstant.EXECUTE_NORMAL_TIME);
                    return exeTask.get(dto.getTaskSnr());
                } catch (InterruptedException e) {
                    executeDto.setOk(false);
                    return exeTask.get(dto.getTaskSnr());
                }
            }
        }, executorService);
        try {
            TaskExecuteDto taskExecuteDto = future.get();
            executeDto.setOk(taskExecuteDto.isOk());
            executeDto.setResult(taskExecuteDto.getResult());
        } catch (Exception e) {
            executeDto.setOk(false);
        }
        exeTask.remove(executeDto.getTaskSnr());
        log.debug("result:{}", JSON.toJSONString(executeDto));
        return executeDto;
    }

}
