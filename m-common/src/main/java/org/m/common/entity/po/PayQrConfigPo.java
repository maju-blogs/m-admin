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
 * 支付二维码 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "支付二维码")
@Table(value = "pay_qr_config")
public class PayQrConfigPo {

    /**
     * 主键
     */
    @Schema(description = "主键")
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 支付金额
     */
    @Schema(description = "支付金额")
    @Column(value = "pay_amount")
    private BigDecimal payAmount;

    /**
     * 二维码图片
     */
    @Schema(description = "二维码图片")
    @Column(value = "qr_base64")
    private String qrBase64;

    /**
     * 原图
     */
    @Schema(description = "原图")
    @Column(value = "qr_old_base64")
    private String qrOldBase64;

    /**
     * 二维码备注号
     */
    @Schema(description = "二维码备注号")
    @Column(value = "qr_mark")
    private String qrMark;

    /**
     * 美化方式
     */
    @Schema(description = "美化方式")
    @Column(value = "qr_logo_type")
    private Integer qrLogoType;

    /**
     * 支付方式 1 微信
     */
    @Schema(description = "支付方式 1 微信")
    @Column(value = "qr_type")
    private Integer qrType;

    /**
     * 描述信息
     */
    @Schema(description = "描述信息")
    @Column(value = "desc")
    private String desc;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @Column(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Column(value = "create_time")
    private Date createTime;


}
