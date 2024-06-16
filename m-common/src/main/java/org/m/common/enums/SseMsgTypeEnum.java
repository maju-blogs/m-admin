package org.m.common.enums;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public enum SseMsgTypeEnum {
    PAY_RESULT("1"),
    ;

    private String type;

    SseMsgTypeEnum(String type) {
        this.type = type;
    }

    public static String getSSEMsg(String msg, SseMsgTypeEnum sseMsgTypeEnum) {
        JSONObject object = new JSONObject();
        object.put("type", sseMsgTypeEnum.getType());
        object.put("msg", msg);
        return object.toString();
    }
}
