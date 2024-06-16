package org.m.web.controller.task;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.m.common.entity.dto.TaskExecuteDto;
import org.m.common.entity.po.TaskUploadRecordPo;
import org.m.web.service.ITaskManager;
import org.m.web.service.ITaskUploadRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

import cn.dev33.satoken.util.SaResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.m.common.entity.po.table.TaskUploadRecordPoTableDef.TASK_UPLOAD_RECORD_PO;

/**
 * 任务上报记录 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/taskUploadRecord")
@Tag(name = "任务上报记录控制层")
public class TaskUploadRecordController {

    @Resource
    private ITaskUploadRecordService taskUploadRecordService;

    @Resource
    private ITaskManager iTaskManager;

    /**
     * 添加 任务上报记录
     *
     * @param taskUploadRecord 任务上报记录
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加任务上报记录")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键"),

            @Parameter(name = "taskId", description = "任务id"),

            @Parameter(name = "taskName", description = "任务名称"),

            @Parameter(name = "taskDesc", description = "任务描述"),

            @Parameter(name = "taskHex", description = "任务文件"),

            @Parameter(name = "uploadClient", description = "上报客户端"),

            @Parameter(name = "createTime", description = "上报时间"),

            @Parameter(name = "status", description = "状态")
    })

    public SaResult save(@RequestBody TaskUploadRecordPo taskUploadRecord) {
        return SaResult.data(taskUploadRecordService.save(taskUploadRecord));
    }


    /**
     * 根据主键删除任务上报记录
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PostMapping("/remove/{id}")
    @Operation(summary = "根据主键删除任务上报记录")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult remove(@PathVariable Serializable id) {
        return SaResult.data(taskUploadRecordService.removeById(id));
    }


    /**
     * 根据主键更新任务上报记录
     *
     * @param taskUploadRecord 任务上报记录
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/update")
    @Operation(summary = "根据主键更新任务上报记录")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true),

            @Parameter(name = "taskId", description = "任务id"),

            @Parameter(name = "taskName", description = "任务名称"),

            @Parameter(name = "taskDesc", description = "任务描述"),

            @Parameter(name = "taskHex", description = "任务文件"),

            @Parameter(name = "uploadClient", description = "上报客户端"),

            @Parameter(name = "createTime", description = "上报时间"),

            @Parameter(name = "status", description = "状态")
    })
    public SaResult update(@RequestBody TaskUploadRecordPo taskUploadRecord) {
        return SaResult.data(taskUploadRecordService.updateById(taskUploadRecord));
    }


    /**
     * 查询所有任务上报记录
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有任务上报记录")
    public SaResult list() {
        return SaResult.data(taskUploadRecordService.list());
    }


    /**
     * 根据任务上报记录主键获取详细信息。
     *
     * @param id taskUploadRecord主键
     * @return 任务上报记录详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据任务上报记录主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult getInfo(@PathVariable Serializable id) {
        return SaResult.data(taskUploadRecordService.getById(id));
    }


    /**
     * 分页查询任务上报记录
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询任务上报记录")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public SaResult page(Page<TaskUploadRecordPo> page) {
        return SaResult.data(taskUploadRecordService.page(page,
                QueryWrapper.create().orderBy(TASK_UPLOAD_RECORD_PO.CREATE_TIME, false)));
    }


    /**
     * 执行一次任务
     *
     * @param taskUploadRecord 执行一次任务
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/doTask")
    @Operation(summary = "执行一次任务")
    @Parameters(value = {

            @Parameter(name = "taskId", description = "任务id"),

            @Parameter(name = "uploadClient", description = "上报客户端"),
    })
    public SaResult doTask(@RequestBody TaskUploadRecordPo taskUploadRecord) {
        TaskExecuteDto taskExecuteDto = iTaskManager.doTask(taskUploadRecord.getTaskId(), taskUploadRecord.getUploadClient());
        if (taskExecuteDto.isOk()) {
            return SaResult.ok();
        }
        return SaResult.error();
    }

    /**
     * 下载任务
     *
     * @param taskUploadRecord 下载任务
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/downloadTask")
    @Operation(summary = "下载任务")
    @Parameters(value = {

            @Parameter(name = "taskId", description = "任务id"),
    })
    public SaResult downloadTask(@RequestBody TaskUploadRecordPo taskUploadRecord) {
        Boolean b = iTaskManager.downloadTask(taskUploadRecord.getTaskId(), taskUploadRecord.getUploadClient());
        if (b) {
            return SaResult.error();
        }
        return SaResult.ok();
    }
}