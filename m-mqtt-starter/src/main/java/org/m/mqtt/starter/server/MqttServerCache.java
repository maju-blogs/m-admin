package org.m.mqtt.starter.server;

import cn.hutool.core.util.StrUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MqttServerCache {
    /**
     * 功能描述:存放主题和其订阅的客户端集合
     */
    public static final ConcurrentHashMap<String, HashSet<String>> subMap = new ConcurrentHashMap<String, HashSet<String>>();


    /**
     * 功能描述:存放订阅是的服务质量等级，只有发送小于或等于这个服务质量的消息给订阅者
     */
    public static final ConcurrentHashMap<String, MqttQoS> qoSMap = new ConcurrentHashMap<String, MqttQoS>();

    /**
     * 功能描述:存放客户端和其所订阅的主题集合，用来在客户端断开的时候删除订阅中的客户端
     */
    public static final ConcurrentHashMap<String, Set<String>> ctMap = new ConcurrentHashMap<String, Set<String>>();

    /**
     * 功能描述:存放需要缓存的消息，一边发送给新订阅的客户端
     */
    public static final ConcurrentHashMap<String, MqttPublishMessage> cacheRetainedMessages = new ConcurrentHashMap<String, MqttPublishMessage>();

    /**
     * 功能描述:缓存需要重复发送的消息，以便在收到ack的时候将消息内存释放掉
     */
    public static final ConcurrentHashMap<String, ByteBuf> cacheRepeatMessages = new ConcurrentHashMap<String, ByteBuf>();

    public static final ConcurrentHashMap<String, ChannelHandlerContext> clientChanelMap = new ConcurrentHashMap<String, ChannelHandlerContext>();


    /**
     * 客户端连接信息
     */

    public static final ConcurrentHashMap<String, ConcurrentHashMap<String, String>> clientMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();


    public static ConcurrentHashMap<String, ConcurrentHashMap<String, String>> getAllClientMap() {
        clientMap.forEach((k, v) -> {
            String topic = "";
            for (Map.Entry<String, HashSet<String>> entry : subMap.entrySet()) {
                if (entry.getValue().contains(k)) {
                    topic += "," + entry.getKey();
                }
            }
            v.put("topic", StrUtil.sub(topic, 1, topic.length()));
        });
        return clientMap;
    }

    public static ConcurrentHashMap<String, String> getOneClientByClientId(String clientId) {
        ConcurrentHashMap<String, String> result = new ConcurrentHashMap<>();
        if (StrUtil.isEmpty(clientId)) {
            return result;
        }
        clientMap.forEach((k, v) -> {
            String topic = "";
            if (clientId.equals(v.get("clientId"))) {
                for (Map.Entry<String, HashSet<String>> entry : subMap.entrySet()) {
                    if (entry.getValue().contains(k)) {
                        topic += "," + entry.getKey();
                    }
                }
                v.put("topic", StrUtil.sub(topic, 1, topic.length()));
                result.putAll(v);
            }

        });
        return result;
    }
}
