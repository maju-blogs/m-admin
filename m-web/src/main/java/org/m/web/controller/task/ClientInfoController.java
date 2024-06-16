package org.m.web.controller.task;

import com.mybatisflex.core.paginate.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.m.pay.service.IClientInfoService;
import org.m.common.entity.po.ClientInfoPo;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import cn.dev33.satoken.util.SaResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 客户端表 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/pay/clientInfo")
@Tag(name = "客户端表控制层")
@Slf4j
public class ClientInfoController {

    @Resource
    private IClientInfoService clientInfoService;

    /**
     * 添加 客户端表
     *
     * @param clientInfo 客户端表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加客户端表")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键"),

            @Parameter(name = "clientId", description = "链接id"),

            @Parameter(name = "clientName", description = "连接名称"),

            @Parameter(name = "clientDesc", description = "客户端描述"),

            @Parameter(name = "clientIcon", description = "客户端图片"),

            @Parameter(name = "createTime", description = "创建时间"),

            @Parameter(name = "status", description = "状态")
    })

    public SaResult save(@RequestBody ClientInfoPo clientInfo) {
        return SaResult.data(clientInfoService.save(clientInfo));
    }


    /**
     * 根据主键删除客户端表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PostMapping("/remove/{id}")
    @Operation(summary = "根据主键删除客户端表")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult remove(@PathVariable Serializable id) {
        return SaResult.data(clientInfoService.removeById(id));
    }


    /**
     * 根据主键更新客户端表
     *
     * @param clientInfo 客户端表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/update")
    @Operation(summary = "根据主键更新客户端表")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true),

            @Parameter(name = "clientId", description = "链接id"),

            @Parameter(name = "clientName", description = "连接名称"),

            @Parameter(name = "clientDesc", description = "客户端描述"),

            @Parameter(name = "clientIcon", description = "客户端图片"),

            @Parameter(name = "createTime", description = "创建时间"),

            @Parameter(name = "status", description = "状态")
    })
    public SaResult update(@RequestBody ClientInfoPo clientInfo) {
        return SaResult.data(clientInfoService.updateById(clientInfo));
    }


    /**
     * 查询所有客户端表
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有客户端表")
    public SaResult list() {
        List<ClientInfoPo> list = clientInfoService.getAllClient();
        return SaResult.data(list);
    }


    /**
     * 根据客户端表主键获取详细信息。
     *
     * @param id clientInfo主键
     * @return 客户端表详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据客户端表主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult getInfo(@PathVariable Serializable id) {
        return SaResult.data(clientInfoService.getById(id));
    }


    /**
     * 分页查询客户端表
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询客户端表")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public SaResult page(Page<ClientInfoPo> page) {
        return SaResult.data(clientInfoService.page(page));
    }


    /**
     * 获取所有订阅地址
     *
     * @return 所有数据
     */
    @GetMapping("/getAllTopic")
    @Operation(summary = "获取所有订阅地址")
    public SaResult getAllTopic() {
        Set<String> set = clientInfoService.getAllTopic();
        return SaResult.data(set);
    }
}