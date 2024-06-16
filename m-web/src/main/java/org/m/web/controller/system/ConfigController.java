package org.m.web.controller.system;

import cn.dev33.satoken.util.SaResult;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.m.common.entity.po.ConfigPo;
import org.m.web.service.IConfigService;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * 配置表 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/config")
@Tag(name = "配置表控制层")
public class ConfigController {

    @Resource
    private IConfigService configService;

    /**
     * 添加 配置表
     *
     * @param config 配置表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加配置表")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键"),
            @Parameter(name = "pythonBin", description = "python安装目录"),
            @Parameter(name = "pythonPath", description = "python脚本路径"),
            @Parameter(name = "giteeAccount", description = "gitee账号"),
            @Parameter(name = "giteePassword", description = "gitee密码"),
            @Parameter(name = "giteeAddress", description = "gitee仓库地址"),
            @Parameter(name = "giteeBranch", description = "gitee分支"),
            @Parameter(name = "chromiumPath", description = "浏览器地址"),
            @Parameter(name = "status", description = "状态")})
    public SaResult save(@RequestBody ConfigPo config) {
        return SaResult.data(configService.save(config));
    }


    /**
     * 根据主键删除配置表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PostMapping("/remove/{id}")
    @Operation(summary = "根据主键删除配置表")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult remove(@PathVariable Serializable id) {
        return SaResult.data(configService.removeById(id));
    }


    /**
     * 根据主键更新配置表
     *
     * @param config 配置表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/update")
    @Operation(summary = "根据主键更新配置表")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true),
            @Parameter(name = "pythonBin", description = "python安装目录"),
            @Parameter(name = "pythonPath", description = "python脚本路径"),
            @Parameter(name = "giteeAccount", description = "gitee账号"),
            @Parameter(name = "giteePassword", description = "gitee密码"),
            @Parameter(name = "giteeAddress", description = "gitee仓库地址"),
            @Parameter(name = "giteeBranch", description = "gitee分支"),
            @Parameter(name = "chromiumPath", description = "浏览器地址"),
            @Parameter(name = "status", description = "状态")})
    public SaResult update(@RequestBody ConfigPo config) {
        return SaResult.data(configService.updateById(config));
    }


    /**
     * 查询所有配置表
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有配置表")
    public SaResult list() {
        return SaResult.data(configService.list());
    }


    /**
     * 根据配置表主键获取详细信息。
     *
     * @param id config主键
     * @return 配置表详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据配置表主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult getInfo(@PathVariable Serializable id) {
        return SaResult.data(configService.getById(id));
    }


    /**
     * 分页查询配置表
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询配置表")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public SaResult page(Page<ConfigPo> page) {
        return SaResult.data(configService.page(page));
    }
}