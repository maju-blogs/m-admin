package org.m.pay.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.m.mqtt.starter.client.MqttClientProperties;
import org.m.mqtt.starter.server.MqttMsgBack;
import org.m.mqtt.starter.server.MqttServerCache;
import org.m.pay.mapper.ClientInfoMapper;
import org.springframework.stereotype.Service;
import org.m.pay.service.IClientInfoService;
import org.m.common.entity.po.ClientInfoPo;
import com.mybatisflex.spring.service.impl.ServiceImpl;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 客户端表 服务层实现。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Service
@Slf4j
public class ClientInfoServiceImpl extends ServiceImpl<ClientInfoMapper, ClientInfoPo> implements IClientInfoService {

    @Resource
    private MqttClientProperties mqttClientProperties;

    @Override
    public List<ClientInfoPo> getAllClient() {
        List<ClientInfoPo> list = list();
        List<ClientInfoPo> clientInfoPos = new ArrayList<>();
        MqttServerCache.clientMap.forEach((k, v) -> {
            ClientInfoPo.ClientInfoPoBuilder builder = ClientInfoPo.builder();
            builder.add(0).online(1).channelId(k);
            builder.clientId(v.get("clientId"))
                    .clientIP(v.get("ip"))
                    .build();
            clientInfoPos.add(builder.build());

        });
        Map<String, ClientInfoPo> clientInfoPoMap =
                clientInfoPos.stream().collect(Collectors.toMap(ClientInfoPo::getClientId, Function.identity(), (k1, k2) -> k2));
        list.forEach(item -> {
            item.setOnline(0);
            item.setAdd(1);
            ClientInfoPo client = clientInfoPoMap.get(item.getClientId());
            if (null != client) {
                item.setOnline(1);
                item.setChannelId(client.getChannelId());
                item.setClientIP(client.getClientIP());
                clientInfoPoMap.remove(client.getClientId());
            }
        });
        list.addAll(clientInfoPoMap.values());
        list.forEach(item -> {
            if (item.getClientId().startsWith(mqttClientProperties.getClientId())) {
                item.setClientType(1);
            } else {
                item.setClientType(0);
            }
            if (null != item.getChannelId()) {
                MqttServerCache.subMap.forEach((k, v) -> {
                    if (v.contains(item.getChannelId())) {
                        String topics = item.getTopics();
                        if (StrUtil.isEmpty(topics)) {
                            topics = k;
                        } else {
                            topics += "," + k;
                        }
                        item.setTopics(topics);
                    }
                });
            }
        });
        return list;
    }

    @Override
    public Set<String> getAllTopic() {
        Set<String> topics = new HashSet<>();
        List<ClientInfoPo> allClient = getAllClient();
        Set<String> collect = allClient.stream().filter(item -> item.getOnline() == 1
                        && StrUtil.isNotEmpty(item.getTopics()))
                .map(ClientInfoPo::getTopics).collect(Collectors.toSet());
        collect.forEach(item -> {
            CollUtil.addAll(topics, item.split(","));
        });
        return topics;
    }
}