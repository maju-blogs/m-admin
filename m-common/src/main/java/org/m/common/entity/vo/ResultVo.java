package org.m.common.cache.entity.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;


@Data
@Builder
@ToString
public class ResultVo {
    private String id;
    private String question;
    private String result;
}
