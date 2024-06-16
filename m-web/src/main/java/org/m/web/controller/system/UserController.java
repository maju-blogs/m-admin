package org.m.web.controller.system;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.m.common.entity.dto.UserDto;
import org.m.common.entity.po.UserPo;
import org.m.web.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.m.common.entity.po.table.UserPoTableDef.USER_PO;


/**
 * 用户表 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户表控制层")
public class UserController {

    @Autowired
    private IUserService userService;

    private Map<String, String> codeMap = new HashMap<String, String>();

    /**
     * 添加 用户表
     *
     * @param user 用户表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加用户表")
    @Parameters(value = {
            @Parameter(name = "id", description = "id"),
            @Parameter(name = "userName", description = "用户名"),
            @Parameter(name = "account", description = "账号"),
            @Parameter(name = "password", description = "密码"),
            @Parameter(name = "createTime", description = ""),
            @Parameter(name = "updateTime", description = ""),
            @Parameter(name = "status", description = "状态")})
    public boolean save(@RequestBody UserPo user) {
        user.setStatus(1);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        return userService.save(user);
    }


    /**
     * 根据主键删除用户表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    @Operation(summary = "根据主键删除用户表")
    @Parameters(value = {
            @Parameter(name = "id", description = "id", required = true)
    })
    public boolean remove(@PathVariable Serializable id) {
        return userService.removeById(id);
    }


    /**
     * 根据主键更新用户表
     *
     * @param user 用户表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/update")
    @Operation(summary = "根据主键更新用户表")
    @Parameters(value = {
            @Parameter(name = "id", description = "id", required = true),
            @Parameter(name = "userName", description = "用户名"),
            @Parameter(name = "account", description = "账号"),
            @Parameter(name = "password", description = "密码"),
            @Parameter(name = "createTime", description = ""),
            @Parameter(name = "updateTime", description = ""),
            @Parameter(name = "status", description = "状态")})
    public boolean update(@RequestBody UserPo user) {
        return userService.updateById(user);
    }


    /**
     * gebn
     *
     * @param user 用户表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/updateOcr")
    @Operation(summary = "根据主键更新用户表")
    @Parameters(value = {
            @Parameter(name = "clientId", description = "百度clientId"),
            @Parameter(name = "clientSecret", description = "百度clientSecret")})
    public boolean updateOcr(@RequestBody UserPo user) {
        return userService.updateOcr(user);
    }


    /**
     * 查询所有用户表
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有用户表")
    public List<UserPo> list() {
        List<UserPo> list = userService.list();
        list.forEach(item -> {
            item.setBdOcrClientSecret("");
            item.setBdOrcClientId("");
        });
        return userService.list();
    }


    /**
     * 根据用户表主键获取详细信息。
     *
     * @param id user主键
     * @return 用户表详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据用户表主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "id", required = true)
    })
    public UserPo getInfo(@PathVariable Serializable id) {
        UserPo userPo = userService.getById(id);
        userPo.setBdOcrClientSecret("");
        userPo.setBdOrcClientId("");
        return userPo;
    }


    /**
     * 分页查询用户表
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询用户表")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public Page<UserPo> page(Page<UserPo> page) {
        Page<UserPo> result = userService.page(page);
        result.getRecords().forEach(item -> {
            item.setBdOcrClientSecret("");
            item.setBdOrcClientId("");
        });
        return userService.page(result);
    }


    // 测试登录，浏览器访问： http://localhost:8081/user/doLogin?username=zhang&password=123456
    @RequestMapping("doLogin")
    public SaResult doLogin(@RequestBody UserDto dto, HttpServletRequest request) {
        String id = request.getSession().getId();
        String cacheCode = codeMap.get(id);
        if (StringUtil.isBlank(cacheCode) || !cacheCode.equals(dto.getCode())) {
            return SaResult.error("验证码错误");
        }
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        UserPo user = userService.getOne(QueryWrapper.create().from(USER_PO).where(USER_PO.ACCOUNT.eq(dto.getUserName())));
        if (null != user && user.getPassword().equals(dto.getPassword())) {
            StpUtil.login(user.getId(), new SaLoginModel()
                    .setDevice("PC")
                    // 此次登录的客户端设备类型, 用于[同端互斥登录]时指定此次登录的设备类型
                    .setIsLastingCookie(true)        // 是否为持久Cookie（临时Cookie在浏览器关闭时会自动删除，持久Cookie在重新打开后依然存在）
                    .setTimeout(60 * 60 * 24 * 7)    // 指定此次登录token的有效期, 单位:秒 （如未指定，自动取全局配置的 timeout 值）
                    .setExtra("name", user.getUserName())    // Token挂载的扩展参数 （此方法只有在集成jwt插件时才会生效）
                    .setExtra("userId", user.getId())    // Token挂载的扩展参数 （此方法只有在集成jwt插件时才会生效）
                    .setIsWriteHeader(false));
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(tokenInfo));
            jsonObject.put("userName", StpUtil.getExtra("name"));
            return SaResult.data(jsonObject);
        }
        return SaResult.error("登录失败");
    }

    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

    @RequestMapping("updatePassword")
    public SaResult updatePassword(@RequestBody UserDto dto) {
        String userId = StpUtil.getExtra("userId").toString();
        UserPo userPo = userService.getById(Integer.parseInt(userId));
        if (!userPo.getPassword().equals(dto.getOldPassword())) {
            return SaResult.error("原密码不匹配");
        }
        userPo.setPassword(dto.getNewPassword());
        userService.updateById(userPo);
        return SaResult.ok();
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("getTokenInfo")
    public SaResult getTokenInfo() {
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(tokenInfo));
        jsonObject.put("userName", StpUtil.getExtra("name"));
        jsonObject.put("userId", StpUtil.getExtra("userId"));
        return SaResult.data(jsonObject);
    }

    @SaIgnore
    @RequestMapping("getCode")
    public SaResult getCode(HttpServletRequest request) {
//        String code = "1234";
        String id = request.getSession().getId();
        if (id == null || "".equals(id)) {
            return SaResult.error();
        }
        String code = RandomUtil.randomNumbers(4);
        codeMap.put(id, code);
        return SaResult.data(code);
    }
}