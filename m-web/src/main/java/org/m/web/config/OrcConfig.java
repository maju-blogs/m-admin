package org.m.web.config;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.m.common.entity.po.UserPo;
import org.m.web.service.IUserService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.m.common.entity.po.table.UserPoTableDef.USER_PO;


@Slf4j
public class OrcConfig {

    private static IUserService userService;
    public static String ORC_TOKEN = "";
    public static String CLIENT_ID = "";
    public static String CLIENT_SECRET = "";

    public static String getToken() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("grant_type", "client_credentials");
            params.put("client_id", CLIENT_ID);
            params.put("client_secret", CLIENT_SECRET);
            String result = HttpUtil.post("https://aip.baidubce.com/oauth/2.0/token", params);
            JSONObject object = JSONObject.parseObject(result);
            ORC_TOKEN = object.getString("access_token");
        } catch (Exception e) {
            log.error("百度ocr获取token异常", e);
        }
        if (StringUtil.isBlank(CLIENT_ID) || StringUtil.isBlank(CLIENT_SECRET)) {
            if (null == userService) {
                userService = SpringUtil.getBean(IUserService.class);
            }
            UserPo one = userService.getOne(QueryWrapper.create()
                    .from(USER_PO));
            CLIENT_ID = one.getBdOrcClientId();
            CLIENT_SECRET = one.getBdOcrClientSecret();
        }
        return ORC_TOKEN;
    }

    public static JSONObject doOcr(String imgEncode) {
        if (StringUtil.isBlank(ORC_TOKEN)) {
            ORC_TOKEN = getToken();
        }
        try {

            imgEncode = imgEncode.replace("data:image/png;base64,", "");
            Map<String, Object> params = new HashMap<>();
            params.put("access_token", ORC_TOKEN);
            params.put("image", imgEncode);
            String post = HttpUtil.post(" https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic", params);
            return JSONObject.parseObject(post);
        } catch (Exception e) {
            log.error("百度ocr识别异常", e);

        }
        return null;
    }

}
