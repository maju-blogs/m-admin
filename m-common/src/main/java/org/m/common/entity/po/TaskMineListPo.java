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

import java.util.Date;
import java.lang.String;
import java.lang.Integer;

/**
 * 我的任务 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "我的任务")
@Table(value = "task_mine_list")
public class TaskMineListPo {

    /**
     * 主键
     */
    @Schema(description = "主键")
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 任务id
     */
    @Schema(description = "任务id")
    @Column(value = "task_id")
    private String taskId;

    /**
     * 任务名称
     */
    @Schema(description = "任务名称")
    @Column(value = "task_name")
    private String taskName;

    /**
     * 任务描述
     */
    @Schema(description = "任务描述")
    @Column(value = "task_desc")
    private String taskDesc;

    /**
     * 任务文件
     */
    @Schema(description = "任务文件")
    @Column(value = "task_hex")
    private String taskHex;

    /**
     * 添加时间
     */
    @Schema(description = "添加时间")
    @Column(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    @Column(value = "update_time")
    private Date updateTime;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @Column(value = "status")
    private Integer status;


}
