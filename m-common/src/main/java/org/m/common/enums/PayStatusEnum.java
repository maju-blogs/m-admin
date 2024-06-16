package org.m.common.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum PayStatusEnum {
    PAY_NOT(0, "未支付"),
    PAY_ING(1, "支付中"),
    PAY_SUCCESS(2, "支付成功"),
    PAY_FAIL(3, "支付失败"),
    ;

    PayStatusEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    private Integer type;
    private String name;

    public static String getNameForType(Integer type) {
        for (PayStatusEnum value : PayStatusEnum.values()) {
            if (value.type == type) {
                return value.name;
            }
        }
        return null;
    }

}
