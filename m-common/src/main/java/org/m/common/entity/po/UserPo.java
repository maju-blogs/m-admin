package org.m.common.entity.po;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 用户表 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
@Builder
@Schema(name = "用户表")
@Table(value = "py_user")
public class UserPo {

    /**
     * id
     */
    @Schema(description = "id")
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @Column(value = "user_name")
    private String userName;

    /**
     * 账号
     */
    @Schema(description = "账号")
    @Column(value = "account")
    private String account;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @Column(value = "password")
    private String password;

    /**
     * 百度ocrId
     */
    @Schema(description = "百度ocrId")
    @Column(value = "bd_orc_client_id")
    private String bdOrcClientId;

    /**
     * 百度ocr secret
     */
    @Schema(description = "百度ocr secret")
    @Column(value = "bd_ocr_client_secret")
    private String bdOcrClientSecret;

    @Schema(description = "")
    @Column(value = "create_time")
    private Date createTime;

    @Schema(description = "")
    @Column(value = "update_time")
    private Date updateTime;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @Column(value = "status")
    private Integer status;


}
