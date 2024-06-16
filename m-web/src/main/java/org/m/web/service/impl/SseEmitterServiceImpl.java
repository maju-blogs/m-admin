package org.m.web.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.m.common.entity.vo.MessageVo;
import org.m.web.config.SseEmitterUTF8;
import org.m.common.inter.ISseEmitterService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
@Service
public class SseEmitterServiceImpl implements ISseEmitterService {
    /**
     * 容器，保存连接，用于输出返回 ;可使用其他方法实现
     */
    private static final Map<String, SseEmitter> sseCache = new ConcurrentHashMap<>();


    /**
     * 根据客户端id获取SseEmitter对象
     *
     * @param clientId 客户端ID
     */
    @Override
    public SseEmitter getSseEmitterByClientId(String clientId) {
        return sseCache.get(clientId);
    }


    /**
     * 创建连接
     *
     * @param clientId 客户端ID
     */
    @Override
    public SseEmitter createConnect(String clientId) {
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        SseEmitter sseEmitter = new SseEmitterUTF8(0L);
        // 是否需要给客户端推送ID
        if (StrUtil.isBlank(clientId)) {
            clientId = IdUtil.simpleUUID();
        }
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(clientId));     // 长链接完成后回调接口(即关闭连接时调用)
        sseEmitter.onTimeout(timeoutCallBack(clientId));        // 连接超时回调
        sseEmitter.onError(errorCallBack(clientId));          // 推送消息异常时，回调方法
        sseCache.put(clientId, sseEmitter);
        log.info("创建新的sse连接，当前用户：{}    累计用户:{}", clientId, sseCache.size());
        try {
            // 注册成功返回用户信息
            sseEmitter.send(SseEmitter.event().id(String.valueOf(HttpStatus.HTTP_CREATED)).data(clientId, MediaType.APPLICATION_JSON));
        } catch (IOException e) {
            log.error("创建长链接异常，客户端ID:{}   异常信息:{}", clientId, e.getMessage());
        }
        return sseEmitter;
    }

    /**
     * 发送消息给所有客户端
     *
     * @param msg 消息内容
     */
    @Override
    public void sendMessageToAllClient(String msg) {
        if (MapUtil.isEmpty(sseCache)) {
            return;
        }
        // 判断发送的消息是否为空

        for (Map.Entry<String, SseEmitter> entry : sseCache.entrySet()) {
            MessageVo messageVo = new MessageVo();
            messageVo.setClientId(entry.getKey());
            messageVo.setData(msg);
            sendMsgToClientByClientId(entry.getKey(), messageVo, entry.getValue());
        }

    }

    /**
     * 给指定客户端发送消息
     *
     * @param clientId 客户端ID
     * @param msg      消息内容
     */
    @Override
    public void sendMessageToOneClient(String clientId, String msg) {
        MessageVo messageVo = new MessageVo(clientId, msg);
        sendMsgToClientByClientId(clientId, messageVo, sseCache.get(clientId));
    }

    @Override
    public void sendMessageToOneClient(HttpServletRequest request, String msg) {
        Object object = request.getSession().getAttribute("clientId");
        if (null == object) {
            return;
        }
        sendMessageToOneClient(object.toString(), msg);
    }

    /**
     * 关闭连接
     *
     * @param clientId 客户端ID
     */
    @Override
    public void closeConnect(String clientId) {
        SseEmitter sseEmitter = sseCache.get(clientId);
        if (sseEmitter != null) {
            sseEmitter.complete();
            removeUser(clientId);
        }
    }

    /**
     * 推送消息到客户端
     * 此处做了推送失败后，重试推送机制，可根据自己业务进行修改
     *
     * @param clientId  客户端ID
     * @param messageVo 推送信息，此处结合具体业务，定义自己的返回值即可
     **/
    private void sendMsgToClientByClientId(String clientId, MessageVo messageVo, SseEmitter sseEmitter) {
        if (sseEmitter == null) {
            log.error("推送消息失败：客户端{}未创建长链接,失败消息:{}",
                    clientId, messageVo.toString());
            return;
        }
        SseEmitter.SseEventBuilder sendData = SseEmitter.event().id(String.valueOf(HttpStatus.HTTP_OK))
                .data(messageVo, MediaType.APPLICATION_JSON);
        try {
            sseEmitter.send(sendData);
        } catch (IOException e) {
            // 推送消息失败，记录错误日志，进行重推
            log.error("推送消息失败：{},尝试进行重推", messageVo.toString());
            boolean isSuccess = true;
            // 推送消息失败后，每隔10s推送一次，推送5次
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(10000);
                    sseEmitter = sseCache.get(clientId);
                    if (sseEmitter == null) {
                        log.error("{}的第{}次消息重推失败，未创建长链接", clientId, i + 1);
                        continue;
                    }
                    sseEmitter.send(sendData);
                } catch (Exception ex) {
                    log.error("{}的第{}次消息重推失败", clientId, i + 1, ex);
                    continue;
                }
                log.info("{}的第{}次消息重推成功,{}", clientId, i + 1, messageVo.toString());
                return;
            }
        }
    }


    /**
     * 长链接完成后回调接口(即关闭连接时调用)
     *
     * @param clientId 客户端ID
     **/
    private Runnable completionCallBack(String clientId) {
        return () -> {
            log.info("结束连接：{}", clientId);
            removeUser(clientId);
        };
    }

    /**
     * 连接超时时调用
     *
     * @param clientId 客户端ID
     **/
    private Runnable timeoutCallBack(String clientId) {
        return () -> {
            log.info("连接超时：{}", clientId);
            removeUser(clientId);
        };
    }

    /**
     * 推送消息异常时，回调方法
     *
     * @param clientId 客户端ID
     **/
    private Consumer<Throwable> errorCallBack(String clientId) {
        return throwable -> {
            log.error("SseEmitterServiceImpl[errorCallBack]：连接异常,客户端ID:{}", clientId);

            // 推送消息失败后，每隔10s推送一次，推送5次
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(10000);
                    SseEmitter sseEmitter = sseCache.get(clientId);
                    if (sseEmitter == null) {
                        log.error("SseEmitterServiceImpl[errorCallBack]：第{}次消息重推失败,未获取到 {} 对应的长链接", i + 1, clientId);
                        continue;
                    }
                    sseEmitter.send("失败后重新推送");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 移除用户连接
     *
     * @param clientId 客户端ID
     **/
    private void removeUser(String clientId) {
        sseCache.remove(clientId);
        log.info("SseEmitterServiceImpl[removeUser]:移除用户：{}", clientId);
    }
}

