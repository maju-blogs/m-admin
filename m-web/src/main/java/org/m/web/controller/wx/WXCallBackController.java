package org.m.web.controller.wx;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.XML;
import com.alibaba.fastjson2.JSON;
import com.yomahub.liteflow.core.FlowExecutor;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.m.common.entity.dto.WxParamDto;
import org.m.web.config.Config;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
public class WXCallBackController {
    @Resource
    private Config config;
    @Resource
    private FlowExecutor flowExecutor;
    @SaIgnore
    @RequestMapping("/callback")
    public String verifyToken(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String echostr = request.getParameter("echostr");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        if (Objects.isNull(signature) || Objects.isNull(echostr) || Objects.isNull(timestamp) || Objects.isNull(nonce)) {
            try {
                return callBack(request);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        List<String> list = Arrays.asList(config.getToken(), timestamp, nonce);
        list.sort(Comparator.comparing(String::strip));
        String join = String.join("", list);
        String hash = DigestUtil.sha1Hex(join);
        if (signature.equals(hash)) {
            return echostr;
        }
        return ResponseEntity.ok().build().toString();
    }

    public String callBack(HttpServletRequest request) throws IOException {
        String read = IoUtil.read(request.getInputStream(), StandardCharsets.UTF_8);
        log.info("params:{}", read);
        JSONObject jsonObject = XML.toJSONObject(read);
        WxParamDto wxParamDto = JSON.parseObject(jsonObject.getStr("xml"), WxParamDto.class);
        flowExecutor.execute2Resp("chain1", wxParamDto);
        WxParamDto result = WxParamDto.builder()
                .ToUserName(wxParamDto.getFromUserName())
                .FromUserName(wxParamDto.getToUserName()).Content(wxParamDto.getResult())
                .MsgType("text")
                .CreateTime(DateUtil.currentSeconds()).build();
        String xml = XML.toXml(JSONUtil.parseObj(result));
        xml = "<xml>" + xml + "</xml>";
        log.info("result xml:{}", xml);
        return xml;
    }

}
