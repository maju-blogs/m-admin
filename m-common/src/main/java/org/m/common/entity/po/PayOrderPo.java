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

import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Integer;

/**
 * 支付订单表 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "支付订单表")
@Table(value = "pay_order")
public class PayOrderPo {

    /**
     * 主键
     */
    @Schema(description = "主键")
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 支付流水号
     */
    @Schema(description = "支付流水号")
    @Column(value = "pay_id")
    private String payId;

    /**
     * 支付姓名
     */
    @Schema(description = "支付姓名")
    @Column(value = "pay_user_name")
    private String payUserName;

    /**
     * 支付金额
     */
    @Schema(description = "支付金额")
    @Column(value = "pay_amount")
    private BigDecimal payAmount;

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
     * 支付描述
     */
    @Schema(description = "支付描述")
    @Column(value = "pay_desc")
    private String payDesc;

    /**
     * 支付状态
     */
    @Schema(description = "支付状态")
    @Column(value = "pay_status")
    private Integer payStatus;

    /**
     * 支付状态
     */
    @Schema(description = "支付状态")
    @Column(ignore = true)
    private String payStatusName;

    /**
     * 支付时间
     */
    @Schema(description = "支付时间")
    @Column(value = "pay_time")
    private Date payTime;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Column(value = "create_time")
    private Date createTime;


}
