package org.m.common.entity.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WxParamDto {
    private String FromUserName;
    private Long CreateTime;
    private String MsgType;
    private String Content;
    private String MsgId;
    private String MsgDataId;
    private String ToUserName;
    private String result;

}
