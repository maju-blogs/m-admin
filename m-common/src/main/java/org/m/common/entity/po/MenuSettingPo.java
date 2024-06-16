package org.m.common.entity.po;

import com.alibaba.fastjson2.JSONObject;
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
 * 菜单配置 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "菜单配置")
@Table(value = "menu_setting")
public class MenuSettingPo {

    /**
     * 主键
     */
    @Schema(description = "主键")
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 上级
     */
    @Schema(description = "上级")
    @Column(value = "parent")
    private Integer parent;

    /**
     * 路径
     */
    @Schema(description = "路径")
    @Column(value = "path")
    private String path;

    /**
     * 名称
     */
    @Schema(description = "名称")
    @Column(value = "name")
    private String name;

    /**
     * 重定向
     */
    @Schema(description = "重定向")
    @Column(value = "redirect")
    private String redirect;

    /**
     * 组件
     */
    @Schema(description = "组件")
    @Column(value = "component")
    private String component;

    /**
     * 标题
     */
    @Schema(description = "标题")
    @Column(value = "tittle")
    private String tittle;

    /**
     * 是否是链接
     */
    @Schema(description = "是否是链接")
    @Column(value = "is_link")
    private String isLink;

    /**
     * 是否隐藏
     */
    @Schema(description = "是否隐藏")
    @Column(value = "is_hide")
    private boolean isHide;

    /**
     * 是否缓存组件状态
     */
    @Schema(description = "是否缓存组件状态")
    @Column(value = "is_keep_alive")
    private boolean isKeepAlive;

    /**
     * 是否固定在 tagsView 栏上
     */
    @Schema(description = "是否固定在 tagsView 栏上")
    @Column(value = "is_affix")
    private boolean isAffix;

    /**
     * 是否内嵌窗口
     */
    @Schema(description = "是否内嵌窗口")
    @Column(value = "is_iframe")
    private boolean isIframe;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识")
    @Column(value = "roles")
    private String roles;

    /**
     * 图标
     */
    @Schema(description = "图标")
    @Column(value = "icon")
    private String icon;


    /**
     * 菜单信息
     */
    @Schema(description = "菜单信息")
    @Column(ignore = true)
    private JSONObject meta;

    public JSONObject getMeta() {
        meta = new JSONObject();
        meta.put("title",this.tittle);
        meta.put("isLink",this.isLink);
        meta.put("isHide",this.isHide);
        meta.put("isKeepAlive",this.isKeepAlive);
        meta.put("isAffix",this.isAffix);
        meta.put("isIframe",this.isIframe);
        meta.put("roles",this.roles.split(","));
        meta.put("icon",this.icon);
        return meta;
    }


}
