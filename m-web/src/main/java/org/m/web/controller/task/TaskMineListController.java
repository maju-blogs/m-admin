package org.m.web.controller.task;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.m.web.service.ITaskMineListService;
import org.m.common.entity.po.TaskMineListPo;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

import cn.dev33.satoken.util.SaResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.m.common.entity.po.table.TaskMineListPoTableDef.TASK_MINE_LIST_PO;

/**
 * 我的任务 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/taskMineList")
@Tag(name = "我的任务控制层")
public class TaskMineListController {

    @Resource
    private ITaskMineListService taskMineListService;

    /**
     * 添加 我的任务
     *
     * @param taskMineList 我的任务
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加我的任务")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键"),

            @Parameter(name = "taskId", description = "任务id"),

            @Parameter(name = "taskName", description = "任务名称"),

            @Parameter(name = "taskDesc", description = "任务描述"),

            @Parameter(name = "taskHex", description = "任务文件"),

            @Parameter(name = "createTime", description = "添加时间"),

            @Parameter(name = "updateTime", description = "修改时间"),

            @Parameter(name = "status", description = "状态")
    })

    public SaResult save(@RequestBody TaskMineListPo taskMineList) {
        taskMineListService.saveOrUpdateByTaskId(taskMineList);
        return SaResult.ok();
    }


    /**
     * 根据主键删除我的任务
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PostMapping("/remove/{id}")
    @Operation(summary = "根据主键删除我的任务")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult remove(@PathVariable Serializable id) {
        return SaResult.data(taskMineListService.removeById(id));
    }


    /**
     * 根据主键更新我的任务
     *
     * @param taskMineList 我的任务
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/update")
    @Operation(summary = "根据主键更新我的任务")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true),

            @Parameter(name = "taskId", description = "任务id"),

            @Parameter(name = "taskName", description = "任务名称"),

            @Parameter(name = "taskDesc", description = "任务描述"),

            @Parameter(name = "taskHex", description = "任务文件"),

            @Parameter(name = "createTime", description = "添加时间"),

            @Parameter(name = "updateTime", description = "修改时间"),

            @Parameter(name = "status", description = "状态")
    })
    public SaResult update(@RequestBody TaskMineListPo taskMineList) {
        return SaResult.data(taskMineListService.updateById(taskMineList));
    }


    /**
     * 查询所有我的任务
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有我的任务")
    public SaResult list() {
        return SaResult.data(taskMineListService.list());
    }


    /**
     * 根据我的任务主键获取详细信息。
     *
     * @param id taskMineList主键
     * @return 我的任务详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据我的任务主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult getInfo(@PathVariable Serializable id) {
        return SaResult.data(taskMineListService.getById(id));
    }


    /**
     * 分页查询我的任务
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询我的任务")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public SaResult page(Page<TaskMineListPo> page) {
        return SaResult.data(taskMineListService.page(page,
                QueryWrapper.create().orderBy(TASK_MINE_LIST_PO.UPDATE_TIME, false)));
    }
}