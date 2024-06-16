package org.m.common.enums;

import lombok.Getter;

@Getter
public enum MqttMsgTypeEnum {
    DO_TASK("1"),
    ADD_TASK("2"),
    UPLOAD_RESULT("3"),
    UPLOAD_DATA("4"),
    UPLOAD_TASK("5");

    private String type;

    MqttMsgTypeEnum(String type) {
        this.type = type;
    }
}
