package org.m.web.controller.pay;

import cn.dev33.satoken.annotation.SaIgnore;
import com.mybatisflex.core.paginate.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.m.common.entity.po.ClientInfoPo;
import org.m.common.entity.po.TaskMineListPo;
import org.m.common.enums.QrLogoEnum;
import org.m.common.entity.dto.PayDto;
import org.m.mqtt.starter.server.MqttServerCache;
import org.m.pay.service.IClientInfoService;
import org.m.pay.service.IPayServer;
import org.m.web.service.ITaskMineListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.annotation.Resource;
import org.m.pay.service.IPayConfigService;
import org.m.common.entity.po.PayConfigPo;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.dev33.satoken.util.SaResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 支付设置 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/payConfig")
@Tag(name = "支付设置控制层")
public class PayConfigController {

    @Resource
    private IPayConfigService payConfigService;


    @Resource
    private IClientInfoService clientInfoService;

    @Resource
    private ITaskMineListService iTaskMineListService;

    @Resource
    private IPayServer iPayServer;

    /**
     * 添加 支付设置
     *
     * @param payConfig 支付设置
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加支付设置")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键"),

            @Parameter(name = "clientId", description = "客户端id"),

            @Parameter(name = "payType", description = "支付方式"),

            @Parameter(name = "payTimeOut", description = "支付超时剩余时间，秒"),

            @Parameter(name = "taskId", description = "支付任务"),

            @Parameter(name = "status", description = "状态")
    })

    public SaResult save(@RequestBody PayConfigPo payConfig) {
        return SaResult.data(payConfigService.save(payConfig));
    }


    /**
     * 根据主键删除支付设置
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PostMapping("/remove/{id}")
    @Operation(summary = "根据主键删除支付设置")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult remove(@PathVariable Serializable id) {
        return SaResult.data(payConfigService.removeById(id));
    }


    /**
     * 根据主键更新支付设置
     *
     * @param payConfig 支付设置
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/update")
    @Operation(summary = "根据主键更新支付设置")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true),

            @Parameter(name = "clientId", description = "客户端id"),

            @Parameter(name = "payType", description = "支付方式"),

            @Parameter(name = "payTimeOut", description = "支付超时剩余时间，秒"),

            @Parameter(name = "taskId", description = "支付任务"),

            @Parameter(name = "status", description = "状态")
    })
    public SaResult update(@RequestBody PayConfigPo payConfig) {
        return SaResult.data(payConfigService.updateById(payConfig));
    }


    /**
     * 查询所有支付设置
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有支付设置")
    public SaResult list() {
        return SaResult.data(payConfigService.list());
    }


    /**
     * 根据支付设置主键获取详细信息。
     *
     * @param id payConfig主键
     * @return 支付设置详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据支付设置主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult getInfo(@PathVariable Serializable id) {
        return SaResult.data(payConfigService.getById(id));
    }


    /**
     * 分页查询支付设置
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询支付设置")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public SaResult page(Page<PayConfigPo> page) {
        Page<PayConfigPo> result = payConfigService.page(page);
        List<ClientInfoPo> clientInfoPos = clientInfoService.list();
        List<TaskMineListPo> taskList = iTaskMineListService.list();
        Map<String, String> map = clientInfoPos.stream().collect(Collectors.toMap(ClientInfoPo::getClientId, ClientInfoPo::getClientName));
        Map<String, String> taskMap = taskList.stream().collect(Collectors.toMap(TaskMineListPo::getTaskId, TaskMineListPo::getTaskName));
        result.getRecords().forEach(item -> {
            item.setClientName(map.get(item.getClientId()));
            item.setPayTypeName(QrLogoEnum.forType(item.getPayType()).getName());
            item.setTaskName(taskMap.get(item.getTaskId()));

        });
        return SaResult.data(result);
    }

    /**
     * 获取连接信息
     */
    @GetMapping("/getClientInfo")
    @Operation(summary = "获取连接信息")
    public SaResult getClientInfo(String clientId) {
        return SaResult.data(MqttServerCache.getOneClientByClientId(clientId));
    }

    /**
     * 获取支付
     */
    @GetMapping("/getPay")
    @Operation(summary = "获取支付")
    @SaIgnore
    public SaResult getPay(HttpServletRequest request) {
        return SaResult.data(iPayServer.getPay(request));
    }

    /**
     * 获取支付
     */
    @PostMapping("/pay")
    @Operation(summary = "支付")
    @SaIgnore
    public SaResult pay(HttpServletRequest request, PayDto dto) {
        return SaResult.data(iPayServer.pay(request, dto));
    }
}