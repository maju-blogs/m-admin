package org.m.common.entity.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息体
 *
 * @date 2022/5/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageVo {
    /**
     * 客户端id
     */
    private String clientId;
    /**
     * 传输数据体(json)
     */
    private String data;
}

