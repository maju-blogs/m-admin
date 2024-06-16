package org.m.mqtt.starter.client;

import org.m.common.entity.dto.MqttUpDto;

public interface IMqttHandleMsg<T> {
    boolean match(String topic, String data);

    T  handleMsg(String topic, String data);

    T  doTask(MqttUpDto dto, String topic);

}
