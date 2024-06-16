package org.m.web.controller.wx;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.m.web.config.Config;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <desc>
 * 微信回调
 * </desc>
 *
 * @author maju
 * @createDate 2024/1/16
 */
@Slf4j
@RestController
@RequestMapping("/wx")
public class TokenController {

    @Resource
    private Config config;
    @SaIgnore
    @RequestMapping("/token")
    public String token(String data) {
        log.info("====>data:{}", data);
        if (StrUtil.isEmpty(data)) {
            return ResponseEntity.badRequest().build().toString();
        }
        String[] split = data.split("\\|");
        if (!config.getToken().equals(split[0])) {
            return ResponseEntity.badRequest().build().toString();
        }
        JSONObject jsonObject = JSON.parseObject(split[1]);
        String accessToken = jsonObject.getString("access_token");
        if(StrUtil.isEmpty(accessToken)){
            return ResponseEntity.badRequest().build().toString();
        }
        config.setAccessToken(accessToken);
        return ResponseEntity.ok().build().toString();
    }

}
