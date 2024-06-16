package org.m.web.controller.pay;

import cn.dev33.satoken.annotation.SaIgnore;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.m.common.enums.PayStatusEnum;
import org.m.common.enums.QrLogoEnum;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.annotation.Resource;
import org.m.pay.service.IPayOrderService;
import org.m.common.entity.po.PayOrderPo;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.dev33.satoken.util.SaResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.m.common.entity.po.table.PayOrderPoTableDef.PAY_ORDER_PO;

/**
 * 支付订单表 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/payOrder")
@Tag(name = "支付订单表控制层")
public class PayOrderController {

    @Resource
    private IPayOrderService payOrderService;

    /**
     * 添加 支付订单表
     *
     * @param payOrder 支付订单表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加支付订单表")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键"),

            @Parameter(name = "payId", description = "支付流水号"),

            @Parameter(name = "payUserName", description = "支付姓名"),

            @Parameter(name = "payAmount", description = "支付金额"),

            @Parameter(name = "payDesc", description = "支付描述"),

            @Parameter(name = "payStatus", description = "支付状态"),

            @Parameter(name = "payTime", description = "支付时间"),

            @Parameter(name = "createTime", description = "创建时间")
    })

    public SaResult save(@RequestBody PayOrderPo payOrder) {
        return SaResult.data(payOrderService.save(payOrder));
    }


    /**
     * 根据主键删除支付订单表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PostMapping("/remove/{id}")
    @Operation(summary = "根据主键删除支付订单表")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult remove(@PathVariable Serializable id) {
        return SaResult.data(payOrderService.removeById(id));
    }


    /**
     * 根据主键更新支付订单表
     *
     * @param payOrder 支付订单表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/update")
    @Operation(summary = "根据主键更新支付订单表")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true),

            @Parameter(name = "payId", description = "支付流水号"),

            @Parameter(name = "payUserName", description = "支付姓名"),

            @Parameter(name = "payAmount", description = "支付金额"),

            @Parameter(name = "payDesc", description = "支付描述"),

            @Parameter(name = "payStatus", description = "支付状态"),

            @Parameter(name = "payTime", description = "支付时间"),

            @Parameter(name = "createTime", description = "创建时间")
    })
    public SaResult update(@RequestBody PayOrderPo payOrder) {
        return SaResult.data(payOrderService.updateById(payOrder));
    }


    /**
     * 手动确认
     */
    @PostMapping("/manualOrder")
    @Operation(summary = "手动确认")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true),
    })
    public SaResult manualOrder(Integer id) {
        PayOrderPo build = PayOrderPo.builder().id(id).payTime(new Date()).payStatus(PayStatusEnum.PAY_SUCCESS.getType()).build();
        return SaResult.data(payOrderService.updateById(build));
    }


    /**
     * 查询所有支付订单表
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有支付订单表")
    public SaResult list() {
        return SaResult.data(payOrderService.list());
    }


    /**
     * 根据支付订单表主键获取详细信息。
     *
     * @param id payOrder主键
     * @return 支付订单表详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据支付订单表主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult getInfo(@PathVariable Serializable id) {
        return SaResult.data(payOrderService.getById(id));
    }


    /**
     * 分页查询支付订单表
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询支付订单表")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    @SaIgnore
    public SaResult page(Page<PayOrderPo> page) {
        Page<PayOrderPo> result = payOrderService.page(page, QueryWrapper.create()
                .from(PAY_ORDER_PO).orderBy(PAY_ORDER_PO.CREATE_TIME, false));
        result.getRecords().forEach(item -> {
            item.setPayStatusName(PayStatusEnum.getNameForType(item.getPayStatus()));
            item.setPayTypeName(QrLogoEnum.forType(item.getPayType()).getName());
        });
        return SaResult.data(result);
    }
}