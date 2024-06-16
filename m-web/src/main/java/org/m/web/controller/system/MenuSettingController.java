package org.m.web.controller.system;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.m.web.service.IMenuSettingService;
import org.m.common.entity.po.MenuSettingPo;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

import cn.dev33.satoken.util.SaResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 菜单配置 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/menuSetting")
@Tag(name = "菜单配置控制层")
public class MenuSettingController {

    @Autowired
    private IMenuSettingService menuSettingService;

    /**
     * 添加 菜单配置
     *
     * @param menuSetting 菜单配置
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加菜单配置")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键"),

            @Parameter(name = "parent", description = "上级"),

            @Parameter(name = "path", description = "路径"),

            @Parameter(name = "name", description = "名称"),

            @Parameter(name = "redirect", description = "重定向"),

            @Parameter(name = "component", description = "组件"),

            @Parameter(name = "tittle", description = "标题"),

            @Parameter(name = "isLink", description = "是否是链接"),

            @Parameter(name = "isHide", description = "是否隐藏"),

            @Parameter(name = "isKeepAlive", description = "是否缓存组件状态"),

            @Parameter(name = "isAffix", description = "是否固定在 tagsView 栏上"),

            @Parameter(name = "isIframe", description = "是否内嵌窗口"),

            @Parameter(name = "roles", description = "权限标识"),

            @Parameter(name = "icon", description = "图标")
    })

    public SaResult save(@RequestBody MenuSettingPo menuSetting) {
        return SaResult.data(menuSettingService.save(menuSetting));
    }


    /**
     * 根据主键删除菜单配置
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    @Operation(summary = "根据主键删除菜单配置")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult remove(@PathVariable Serializable id) {
        return SaResult.data(menuSettingService.removeById(id));
    }


    /**
     * 根据主键更新菜单配置
     *
     * @param menuSetting 菜单配置
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    @Operation(summary = "根据主键更新菜单配置")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true),

            @Parameter(name = "parent", description = "上级"),

            @Parameter(name = "path", description = "路径"),

            @Parameter(name = "name", description = "名称"),

            @Parameter(name = "redirect", description = "重定向"),

            @Parameter(name = "component", description = "组件"),

            @Parameter(name = "tittle", description = "标题"),

            @Parameter(name = "isLink", description = "是否是链接"),

            @Parameter(name = "isHide", description = "是否隐藏"),

            @Parameter(name = "isKeepAlive", description = "是否缓存组件状态"),

            @Parameter(name = "isAffix", description = "是否固定在 tagsView 栏上"),

            @Parameter(name = "isIframe", description = "是否内嵌窗口"),

            @Parameter(name = "roles", description = "权限标识"),

            @Parameter(name = "icon", description = "图标")
    })
    public SaResult update(@RequestBody MenuSettingPo menuSetting) {
        return SaResult.data(menuSettingService.updateById(menuSetting));
    }


    /**
     * 查询所有菜单配置
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有菜单配置")
    public SaResult list() {
        return SaResult.data(menuSettingService.list());
    }


    /**
     * 根据菜单配置主键获取详细信息。
     *
     * @param id menuSetting主键
     * @return 菜单配置详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据菜单配置主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult getInfo(@PathVariable Serializable id) {
        return SaResult.data(menuSettingService.getById(id));
    }


    /**
     * 分页查询菜单配置
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询菜单配置")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public SaResult page(Page<MenuSettingPo> page) {
        return SaResult.data(menuSettingService.page(page));
    }
}