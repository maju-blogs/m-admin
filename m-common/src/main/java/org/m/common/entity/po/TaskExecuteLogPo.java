package org.m.common.entity.po;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;

import java.lang.String;
import java.lang.Integer;
import java.util.Date;

/**
 * 任务执行记录 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "任务执行记录")
@Table(value = "task_execute_log")
public class TaskExecuteLogPo {

    /**
     * 主键
     */
    @Schema(description = "主键")
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 执行通道
     */
    @Schema(description = "执行通道")
    @Column(value = "topic")
    private String topic;

    /**
     * 下发参数
     */
    @Schema(description = "下发参数")
    @Column(value = "do_params")
    private String doParams;

    /**
     * 执行状态
     */
    @Schema(description = "执行状态")
    @Column(value = "do_status")
    private Integer doStatus;

    /**
     * 执行结果
     */
    @Schema(description = "执行结果")
    @Column(value = "do_result")
    private String doResult;

    /**
     * 执行时间
     */
    @Schema(description = "执行时间")
    @Column(value = "create_time")
    private Date createTime;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @Column(value = "status")
    private Integer status;


}
