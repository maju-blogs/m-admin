package org.m.common.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.function.Consumer;

/**
 * <desc>
 * 执行任务参数
 * </desc>
 *
 * @author maju
 * @createDate 2024/6/11
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskExecuteDto {
    private String taskId;
    private String taskSnr;
    private String result;
    private Integer payStatus;
    private String qrMark;
    private String payId;
    private Integer payType;
    private Date payStartTime;
    private Date payTime;
    private String topic;
    private String regex;
    private String taskType;
    private Date exeExpireTime;
    private boolean isOk;
    private Consumer<TaskExecuteDto> consumer;
}
