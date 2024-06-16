package org.m.web.controller.system;


import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.m.common.entity.vo.MessageVo;
import org.m.common.enums.SseMsgTypeEnum;
import org.m.common.inter.ISseEmitterService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


/**
 * SSE长链接
 */
@RestController
@RequestMapping("/sse")
public class SseEmitterController {

    @Resource
    private ISseEmitterService sseEmitterService;


    @CrossOrigin
    @GetMapping("/createConnect")
    public SseEmitter createConnect(String clientId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object object = session.getAttribute("clientId");
        if (StrUtil.isBlank(clientId) && null == object) {
            clientId = IdUtil.getSnowflakeNextIdStr();
        } else if (StrUtil.isBlank(clientId)) {
            clientId = object.toString();
        }
        session.setAttribute("clientId", clientId);
        return sseEmitterService.createConnect(clientId);
    }

    @CrossOrigin
    @PostMapping("/broadcast")
    public void sendMessageToAllClient(@RequestBody(required = false) String msg) {
        sseEmitterService.sendMessageToAllClient(msg);
    }

    @CrossOrigin
    @PostMapping("/sendMessage")
    @SaIgnore
    public void sendMessageToOneClient(@RequestBody(required = false) MessageVo messageVo) {
        if (messageVo.getClientId().isEmpty()) {
            return;
        }
        sseEmitterService.sendMessageToOneClient(messageVo.getClientId(), SseMsgTypeEnum.getSSEMsg(2 + "", SseMsgTypeEnum.PAY_RESULT));
//        sseEmitterService.sendMessageToOneClient(messageVo.getClientId(), messageVo.getData());
    }

    @CrossOrigin
    @GetMapping("/closeConnect")
    public void closeConnect(@RequestParam(required = true) String clientId) {
        sseEmitterService.closeConnect(clientId);
    }

}

