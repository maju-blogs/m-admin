package org.m.web.controller.system;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.m.common.entity.po.PythonFilePo;
import org.m.web.service.IPythonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 脚本文件 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/pythonFile")
@Tag(name = "脚本文件控制层")
public class PythonFileController {

    @Autowired
    private IPythonFileService pythonFileService;

    /**
     * 添加 脚本文件
     *
     * @param pythonFile 脚本文件
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加脚本文件")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键"),
            @Parameter(name = "code", description = "编码"),
            @Parameter(name = "level", description = "层级"),
            @Parameter(name = "parent", description = "父级id"),
            @Parameter(name = "name", description = "中午名称"),
            @Parameter(name = "fileName", description = "文件名称"),
            @Parameter(name = "pythonCode", description = "脚本代码"),
            @Parameter(name = "status", description = "")})
    public SaResult save(@RequestBody PythonFilePo pythonFile) {
        return SaResult.data(pythonFileService.saveByCode(pythonFile));
    }


    /**
     * 根据主键删除脚本文件
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PostMapping("/remove/{code}")
    @Operation(summary = "根据code删除脚本文件")
    @Parameters(value = {
            @Parameter(name = "code", description = "编码", required = true)
    })
    public SaResult remove(@PathVariable Serializable code) {
        return SaResult.data(pythonFileService.removeByCode(code));
    }


    /**
     * 根据主键更新脚本文件
     *
     * @param pythonFile 脚本文件
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/update")
    @Operation(summary = "根据主键更新脚本文件")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true),
            @Parameter(name = "code", description = "编码"),
            @Parameter(name = "level", description = "层级"),
            @Parameter(name = "parent", description = "父级id"),
            @Parameter(name = "name", description = "中午名称"),
            @Parameter(name = "fileName", description = "文件名称"),
            @Parameter(name = "pythonCode", description = "脚本代码"),
            @Parameter(name = "status", description = "")})
    public SaResult update(@RequestBody PythonFilePo pythonFile) {
        return SaResult.data(pythonFileService.updateById(pythonFile));
    }

    /**
     * 运行
     *
     * @param pythonFile 脚本文件
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/run")
    @Operation(summary = "根据主键更新脚本文件")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)})
    public SaResult run(@RequestBody PythonFilePo pythonFile, HttpServletRequest request) {
        String clientId = request.getSession().getAttribute("clientId").toString();
        return SaResult.data(pythonFileService.run(pythonFile, clientId));
    }


    /**
     * 查询所有脚本文件
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有脚本文件")
    public SaResult list() {
        List<PythonFilePo> list = pythonFileService.list();
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("id");
        List<Tree<String>> treeNodes = TreeUtil.build(list, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId() + "");
                    tree.setParentId(treeNode.getParent() + "");
                    tree.setName(treeNode.getName());
                    tree.putExtra("code", treeNode.getCode());
                    tree.putExtra("type", treeNode.getType());
                    tree.putExtra("level", treeNode.getLevel());
                    tree.putExtra("fileName", treeNode.getFileName());
                });
        return SaResult.data(treeNodes);
    }


    /**
     * 根据脚本文件主键获取详细信息。
     *
     * @param id pythonFile主键
     * @return 脚本文件详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据脚本文件主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult getInfo(@PathVariable Serializable id) {
        return SaResult.data(pythonFileService.getById(id));
    }


    /**
     * 分页查询脚本文件
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询脚本文件")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public SaResult page(Page<PythonFilePo> page) {
        return SaResult.data(pythonFileService.page(page));
    }
}