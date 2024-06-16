package org.m.common.enums;

import lombok.Getter;

@Getter
public enum KeyTypeEnum {
    RESULT("获取结果", 2, "result"),
    DEFAULT("默认", 0, "default"),
    ;

    KeyTypeEnum(String msg, Integer type, String cmp) {
        this.msg = msg;
        this.type = type;
        this.cmp = cmp;
    }

    private String msg;
    private String cmp;
    private Integer type;

    public static KeyTypeEnum forMsg(String msg) {
        for (KeyTypeEnum value : KeyTypeEnum.values()) {
            if (null != msg && msg.startsWith(value.getMsg())) {
                return value;
            }

        }
        return DEFAULT;
    }
}
