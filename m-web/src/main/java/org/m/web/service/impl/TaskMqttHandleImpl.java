package org.m.web.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.m.common.constant.PublicConstant;
import org.m.common.entity.dto.MqttUpDto;
import org.m.common.entity.dto.TaskExecuteDto;
import org.m.common.entity.po.TaskUploadRecordPo;
import org.m.common.enums.MqttMsgTypeEnum;
import org.m.mqtt.starter.client.*;
import org.m.web.service.ITaskUploadRecordService;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@DependsOn("inbound")
public class TaskMqttHandleImpl extends AbsTaskMqttHandle {

    @Resource
    private MyMqttClient myMqttClient;

    @Resource
    private MqttClientProperties mqttClientProperties;

    @Resource
    private ITaskUploadRecordService taskUploadRecordService;

    @PostConstruct
    public void init() {
        myMqttClient.addTopic(this, mqttClientProperties.getTopics());
    }

    @Override
    public boolean match(String topic, String data) {
        MqttUpDto mqttUpDto = JSONObject.parseObject(data, MqttUpDto.class);
        if (MqttMsgTypeEnum.UPLOAD_TASK.getType().equals(mqttUpDto.getType())) {
            return true;
        }
        return super.match(topic, data);
    }

    @Override
    public Object handleMsg(String topic, String data) {
        MqttUpDto mqttUpDto = JSONObject.parseObject(data, MqttUpDto.class);
        if (MqttMsgTypeEnum.UPLOAD_TASK.getType().equals(mqttUpDto.getType())) {
            JSONObject object = JSONObject.parseObject(mqttUpDto.getData().toString());
            TaskUploadRecordPo build = TaskUploadRecordPo.builder()
                    .taskId(object.getString("taskId"))
                    .taskName(object.getString("taskName"))
                    .taskDesc(object.getString("description"))
                    .taskHex(object.getString("fileHex"))
                    .uploadClient(topic.replace(PublicConstant.MQTT_CLIENT_OUT, "")).build();
            taskUploadRecordService.save(build);
            return null;
        }
        return super.handleMsg(topic, data);
    }

    @Override
    public TaskExecuteDto doTask(MqttUpDto dto, String topic) {
        return super.doTask(dto, topic);
    }

}
