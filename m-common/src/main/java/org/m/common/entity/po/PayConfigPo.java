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

/**
 * 支付设置 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "支付设置")
@Table(value = "pay_config")
public class PayConfigPo {

    /**
     * 主键
     */
    @Schema(description = "主键")
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 客户端id
     */
    @Schema(description = "客户端id")
    @Column(value = "client_id")
    private String clientId;

    /**
     * 客户端名称
     */
    @Schema(description = "客户端名称")
    @Column(ignore = true)
    private String clientName;

    /**
     * 支付通道
     */
    @Schema(description = "支付通道")
    @Column(value = "pay_topic")
    private String payTopic;


    /**
     * 匹配数据正则
     */
    @Schema(description = "匹配数据正则")
    @Column(value = "regex")
    private String regex;

    /**
     * 支付方式
     */
    @Schema(description = "支付方式")
    @Column(value = "pay_type")
    private Integer payType;

    /**
     * 支付方式
     */
    @Schema(description = "支付方式")
    @Column(ignore = true)
    private String payTypeName;

    /**
     * 支付超时剩余时间，秒
     */
    @Schema(description = "支付超时剩余时间，秒")
    @Column(value = "pay_time_out")
    private Integer payTimeOut;

    /**
     * 支付任务
     */
    @Schema(description = "支付任务")
    @Column(value = "task_id")
    private String taskId;

    /**
     * 支付任务
     */
    @Schema(description = "支付任务")
    @Column(ignore = true)
    private String taskName;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @Column(value = "status")
    private Integer status;


}
