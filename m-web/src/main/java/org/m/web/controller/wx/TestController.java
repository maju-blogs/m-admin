package org.m.web.controller.wx;

import cn.dev33.satoken.annotation.SaIgnore;
import com.yomahub.liteflow.core.FlowExecutor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.m.common.entity.dto.WxParamDto;
import org.m.mqtt.starter.client.MyMqttClient;
import org.m.pay.handle.PayHandleMsgImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
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
public class TestController {

    @Resource
    private FlowExecutor flowExecutor;

    @Resource
    private MyMqttClient myMqttClient;

    @SaIgnore
    @RequestMapping("/test")
    public ResponseEntity test(@RequestBody WxParamDto wxParamDto) {
        flowExecutor.execute2Resp("chain1", wxParamDto);
        return ResponseEntity.ok(wxParamDto.getResult());
    }


    @SaIgnore
    @RequestMapping("/test1")
    public ResponseEntity test1(String topic) {
        myMqttClient.addTopic(new PayHandleMsgImpl(), topic);
        return ResponseEntity.ok("");
    }

}
