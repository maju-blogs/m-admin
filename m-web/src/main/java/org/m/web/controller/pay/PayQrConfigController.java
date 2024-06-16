package org.m.web.controller.pay;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.m.common.entity.po.UserPo;
import org.m.common.enums.QrLogoEnum;
import org.m.web.config.OrcConfig;
import org.m.web.service.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.m.pay.service.IPayQrConfigService;
import org.m.common.entity.po.PayQrConfigPo;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

import cn.dev33.satoken.util.SaResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.m.common.entity.po.table.PayConfigPoTableDef.PAY_CONFIG_PO;
import static org.m.common.entity.po.table.PayQrConfigPoTableDef.PAY_QR_CONFIG_PO;
import static org.m.common.entity.po.table.UserPoTableDef.USER_PO;

/**
 * 支付二维码 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/payQrConfig")
@Tag(name = "支付二维码控制层")
public class PayQrConfigController {

    @Resource
    private IPayQrConfigService payQrConfigService;

    @Resource
    private IUserService userService;

    /**
     * 添加 支付二维码
     *
     * @param payQrConfig 支付二维码
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加支付二维码")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键"),

            @Parameter(name = "payAmount", description = "支付金额"),

            @Parameter(name = "qrBase64", description = "二维码图片"),

            @Parameter(name = "qrOldBase64", description = "原图"),

            @Parameter(name = "qrMark", description = "二维码备注号"),

            @Parameter(name = "desc", description = "描述信息"),

            @Parameter(name = "status", description = "状态"),

            @Parameter(name = "createTime", description = "创建时间")
    })

    public SaResult save(@RequestBody PayQrConfigPo payQrConfig) {
        long count = payQrConfigService.count(QueryWrapper.create().from(PAY_QR_CONFIG_PO).where(PAY_QR_CONFIG_PO.QR_MARK.eq(payQrConfig.getQrMark())));
        if (count > 0) {
            return SaResult.error("二维码备注号已存在！");
        }
        return payQrConfigService.saveAndSetting(payQrConfig);
    }


    /**
     * 根据主键删除支付二维码
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PostMapping("/remove/{id}")
    @Operation(summary = "根据主键删除支付二维码")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult remove(@PathVariable Serializable id) {
        return SaResult.data(payQrConfigService.removeById(id));
    }


    /**
     * 根据主键更新支付二维码
     *
     * @param payQrConfig 支付二维码
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/update")
    @Operation(summary = "根据主键更新支付二维码")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true),

            @Parameter(name = "payAmount", description = "支付金额"),

            @Parameter(name = "qrBase64", description = "二维码图片"),

            @Parameter(name = "qrOldBase64", description = "原图"),

            @Parameter(name = "qrMark", description = "二维码备注号"),

            @Parameter(name = "desc", description = "描述信息"),

            @Parameter(name = "status", description = "状态"),

            @Parameter(name = "createTime", description = "创建时间")
    })
    public SaResult update(@RequestBody PayQrConfigPo payQrConfig) {
        return SaResult.data(payQrConfigService.updateAndSettingById(payQrConfig));
    }


    /**
     * 查询所有支付二维码
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有支付二维码")
    public SaResult list() {
        return SaResult.data(payQrConfigService.list());
    }


    /**
     * 根据支付二维码主键获取详细信息。
     *
     * @param id payQrConfig主键
     * @return 支付二维码详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据支付二维码主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键", required = true)
    })
    public SaResult getInfo(@PathVariable Serializable id) {
        return SaResult.data(payQrConfigService.getById(id));
    }


    /**
     * 分页查询支付二维码
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询支付二维码")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public SaResult page(Page<PayQrConfigPo> page) {
        return SaResult.data(payQrConfigService.page(page));
    }


    /**
     * 获取美化方式
     *
     * @return 获取美化方式
     */
    @GetMapping("/getAllQrType")
    @Operation(summary = "获取美化方式")
    public SaResult getAllQrType() {
        return SaResult.data(QrLogoEnum.getAllConfig());
    }

    /**
     * 百度OCR
     *
     * @return 百度OCR
     */
    @PostMapping("/baiduOcr")
    @Operation(summary = "百度OCR")
    public SaResult baiduOcr(@RequestBody PayQrConfigPo payQrConfigPo) {
        String imgEncode = payQrConfigPo.getQrOldBase64();
        UserPo one = userService.getOne(QueryWrapper.create().from(USER_PO));
        if (StrUtil.isEmpty(one.getBdOcrClientSecret()) || StrUtil.isEmpty(one.getBdOrcClientId())) {
            return SaResult.error("OCR未配置");
        }
        JSONObject result = OrcConfig.doOcr(imgEncode);
        if (null == result) {
            return SaResult.error("OCR识别失败");
        }
        return SaResult.data(result);
    }
}