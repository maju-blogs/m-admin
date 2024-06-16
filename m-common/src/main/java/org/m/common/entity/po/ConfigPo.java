package org.m.common.entity.po;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 配置表 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
@Builder
@Schema(name = "配置表")
@Table(value = "py_config")
public class ConfigPo {

    /**
     * 主键
     */
    @Schema(description = "主键")
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * python安装目录
     */
    @Schema(description = "python安装目录")
    @Column(value = "python_bin")
    private String pythonBin;

    /**
     * python脚本路径
     */
    @Schema(description = "python脚本路径")
    @Column(value = "python_path")
    private String pythonPath;


    /**
     * python脚本名称
     */
    @Schema(description = "python脚本名称")
    @Column(value = "python_name")
    private String pythonName;

    /**
     * gitee账号
     */
    @Schema(description = "gitee账号")
    @Column(value = "gitee_account")
    private String giteeAccount;

    /**
     * gitee密码
     */
    @Schema(description = "gitee密码")
    @Column(value = "gitee_password")
    private String giteePassword;

    /**
     * gitee仓库地址
     */
    @Schema(description = "gitee仓库地址")
    @Column(value = "gitee_address")
    private String giteeAddress;

    /**
     * gitee分支
     */
    @Schema(description = "gitee分支")
    @Column(value = "gitee_branch")
    private String giteeBranch;

    /**
     * 浏览器地址
     */
    @Schema(description = "浏览器地址")
    @Column(value = "chromium_path")
    private String chromiumPath;

    /**
     * 微信开发者id
     */
    @Schema(description = "微信开发者id")
    @Column(value = "wx_app_id")
    private String wxAppId;

    /**
     * 微信appSecret
     */
    @Schema(description = "微信appSecret")
    @Column(value = "wx_app_secret")
    private String wxAppSecret;

    /**
     * 微信回调token
     */
    @Schema(description = "微信回调token")
    @Column(value = "wx_token")
    private String wxToken;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @Column(value = "status")
    private Integer status;


}
