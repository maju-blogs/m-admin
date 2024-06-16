package org.m.common.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum QrLogoEnum {
    DEFAULT(0, "deflogo.png", "默认"),
    WX(1, "wxlogo.png", "微信"),
    ZHIFUBAO(2, "zfblogo.png", "支付宝"),
    ;

    QrLogoEnum(Integer type, String path, String name) {
        this.type = type;
        this.path = path;
        this.name = name;
    }

    private Integer type;
    private String path;
    private String name;


    public static Object getAllConfig() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (QrLogoEnum value : QrLogoEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", value.type);
            map.put("path", value.path);
            map.put("name", value.name);
            result.add(map);
        }
        return result;
    }

    public static QrLogoEnum forType(Integer type) {
        for (QrLogoEnum value : QrLogoEnum.values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return DEFAULT;
    }
}
