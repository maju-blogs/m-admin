package org.m.common.entity.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;


@Data
@Builder
@ToString
public class GPTDto {
    private String id;
    private String question;
    private String result;
}
