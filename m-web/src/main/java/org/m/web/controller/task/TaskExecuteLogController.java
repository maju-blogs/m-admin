package org.m.web.controller.task;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.m.web.service.ITaskExecuteLogService;
import org.m.common.entity.po.TaskExecuteLogPo;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

import cn.dev33.satoken.util.SaResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.m.common.entity.po.table.TaskExecuteLogPoTableDef.TASK_EXECUTE_LOG_PO;

/**
 * 任务执行记录 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/taskExecuteLog")
@Tag(name = "任务执行记录控制层")
public class TaskExecuteLogController {

    @Resource
    private ITaskExecuteLogService taskExecuteLogService;

    /**
     * 添加 任务执行记录
     *
     * @param taskExecuteLog 任务执行记录
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加任务执行记录")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键"),

            @Parameter(name = "clientId", description = "客户端"),

            @Parameter(name = "doParams", description = "下发参数"),

            @Parameter(name = "doStatus", description = "执行状态"),

            @Parameter(name = "doResult", description = "执行结果"),

            @Parameter(name = "createTime", description = "执行时间"),

            @Parameter(name = "status", description = "状态")
    })

    public SaResult save(@RequestBody TaskExecuteLogPo taskExecuteLog) {
        return SaResult.data(taskExecuteLogService.save(taskExecuteLog));
    }


    /**
     * 根据主键删除任务执行记录
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PostMapping("/remove/{id}")
    @Operation(summary = "根据主键删除任务执行记录")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult remove(@PathVariable Serializable id) {
        return SaResult.data(taskExecuteLogService.removeById(id));
    }


    /**
     * 根据主键更新任务执行记录
     *
     * @param taskExecuteLog 任务执行记录
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/update")
    @Operation(summary = "根据主键更新任务执行记录")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true),

            @Parameter(name = "clientId", description = "客户端"),

            @Parameter(name = "doParams", description = "下发参数"),

            @Parameter(name = "doStatus", description = "执行状态"),

            @Parameter(name = "doResult", description = "执行结果"),

            @Parameter(name = "createTime", description = "执行时间"),

            @Parameter(name = "status", description = "状态")
    })
    public SaResult update(@RequestBody TaskExecuteLogPo taskExecuteLog) {
        return SaResult.data(taskExecuteLogService.updateById(taskExecuteLog));
    }


    /**
     * 查询所有任务执行记录
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有任务执行记录")
    public SaResult list() {
        return SaResult.data(taskExecuteLogService.list());
    }


    /**
     * 根据任务执行记录主键获取详细信息。
     *
     * @param id taskExecuteLog主键
     * @return 任务执行记录详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据任务执行记录主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult getInfo(@PathVariable Serializable id) {
        return SaResult.data(taskExecuteLogService.getById(id));
    }


    /**
     * 分页查询任务执行记录
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询任务执行记录")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public SaResult page(Page<TaskExecuteLogPo> page) {
        return SaResult.data(taskExecuteLogService.page(page, QueryWrapper.create().orderBy(TASK_EXECUTE_LOG_PO.CREATE_TIME, false)));
    }
}