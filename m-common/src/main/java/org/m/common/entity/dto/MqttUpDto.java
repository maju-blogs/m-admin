package org.m.common.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqttUpDto {
    private String topic;
    private String type;
    private String taskSnr;
    private String data;
}
