package org.m.common.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.lang.String;
import java.lang.Integer;

/**
 * 客户端表 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
@Builder
@Schema(name = "客户端表")
@Table(value = "client_info")
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfoPo {

    /**
     * 主键
     */
    @Schema(description = "主键")
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 链接id
     */
    @Schema(description = "链接id")
    @Column(value = "client_id")
    private String clientId;

    /**
     * 链接id
     */
    @Schema(description = "链接IP")
    @Column(value = "client_ip")
    private String clientIP;

    /**
     * 连接名称
     */
    @Schema(description = "连接名称")
    @Column(value = "client_name")
    private String clientName;

    /**
     * 客户端描述
     */
    @Schema(description = "客户端描述")
    @Column(value = "client_desc")
    private String clientDesc;

    /**
     * 客户端图片
     */
    @Schema(description = "客户端图片")
    @Column(value = "client_icon")
    private String clientIcon;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Column(value = "create_time")
    private Date createTime;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @Column(value = "status")
    private Integer status;

    /**
     * 是否添加
     */
    @Schema(description = "是否添加")
    @Column(ignore = true)
    private Integer add;
    /**
     * 是否在线
     */
    @Schema(description = "是否在线")
    @Column(ignore = true)
    private Integer online;

    /**
     * 订阅地址
     */
    @Schema(description = "订阅地址")
    @Column(ignore = true)
    private String topics;

    /**
     * 管道ID
     */
    @Schema(description = "管道ID")
    @Column(ignore = true)
    private String channelId;
    /**
     * 连接类型
     */
    @Schema(description = "连接类型")
    @Column(ignore = true)
    private Integer clientType;

}
