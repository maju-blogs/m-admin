package org.m.mqtt.starter.client;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.m.mqtt.starter.server.MqttServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * <desc>
 * mqtt链接配置类
 * </desc>
 *
 * @author maju
 * @createDate 2024/1/9
 */

@Configuration
@IntegrationComponentScan
@Slf4j
@DependsOn("mqttServer")
public class MyMqttClient {
    @Resource
    private MqttClientProperties mqttClientProperties;
    @Resource
    private MqttServerProperties mqttServerProperties;

    private MqttPahoMessageDrivenChannelAdapter mqttPahoMessageDrivenChannelAdapter;

    private Map<String, List<IMqttHandleMsg>> hadleMsgMap = new ConcurrentHashMap<>();

    /**
     * <desc>
     * 接收消息管道
     * </desc>
     *
     * @param : []
     * @return : org.springframework.messaging.MessageChannel
     * @author : maju
     * @createDate : 2024/1/9
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        ExecutorChannel channel = new ExecutorChannel(executor());
        return channel;
    }

    /**
     * <desc>
     * 多线程异步处理
     * </desc>
     *
     * @param : []
     * @return : org.springframework.messaging.MessageChannel
     * @author : maju
     * @createDate : 2024/1/9
     */
    @Bean
    public Executor executor() {
        ThreadPoolExecutor taskExecutor = new ThreadPoolExecutor(5, 10, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(500));
        return taskExecutor;
    }

    /**
     * <desc>
     * 建立链接
     * </desc>
     *
     * @param : []
     * @return : org.springframework.integration.mqtt.core.MqttPahoClientFactory
     * @author : maju
     * @createDate : 2024/1/9
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        String[] array = mqttClientProperties.getUrl().split(",");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(array);
        options.setUserName(mqttClientProperties.getUsername());
        options.setPassword(mqttClientProperties.getPassword().toCharArray());
        options.setAutomaticReconnect(true);
        if (mqttServerProperties.getIsOpenSsl()) {
            try {
                options.setSocketFactory(SslUtil.getSocketFactory(mqttClientProperties.getClientCrtPath()));
            } catch (Exception e) {
                log.info("加载证书失败", e);
            }
        }
        options.setHttpsHostnameVerificationEnabled(false);
        //接受离线消息
        options.setCleanSession(false);
        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * <desc>
     * 配置client,监听的topic
     * </desc>
     *
     * @param : []
     * @return : org.springframework.integration.core.MessageProducer
     * @author : maju
     * @createDate : 2024/1/9
     */
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(mqttClientProperties.getClientId(), mqttClientFactory());  //对inboundTopics主题进行监听
        adapter.setCompletionTimeout(5000);
        adapter.setQos(1);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setOutputChannel(mqttInputChannel());
        this.mqttPahoMessageDrivenChannelAdapter = adapter;
        return adapter;
    }

    public void addTopic(IMqttHandleMsg handleMsg, String topic) {
        if (hadleMsgMap.containsKey(topic)) {
            if (hadleMsgMap.values().stream().anyMatch(item -> item.getClass() == handleMsg.getClass())) {
                hadleMsgMap.get(topic).add(handleMsg);
            }
        } else {
            mqttPahoMessageDrivenChannelAdapter.addTopic(topic);
            List<IMqttHandleMsg> handleMsgList = new ArrayList<>();
            handleMsgList.add(handleMsg);
            hadleMsgMap.put(topic, handleMsgList);
        }
    }


    /**
     * <desc>
     * MQTT信息通道（生产者）
     * </desc>
     *
     * @param : []
     * @return : org.springframework.messaging.MessageChannel
     * @author : maju
     * @createDate : 2024/1/9
     */
    @Bean
    public MessageChannel mqttOutboundChannel() {
        ExecutorChannel channel = new ExecutorChannel(executor());
        return channel;
    }

    /**
     * <desc>
     * MQTT消息处理器（生产者）
     * </desc>
     *
     * @param : []
     * @return : org.springframework.messaging.MessageHandler
     * @author : maju
     * @createDate : 2024/1/9
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(mqttClientProperties.getClientIdOut(), mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(mqttClientProperties.getAckTopics());
        messageHandler.setAsyncEvents(true); // 消息发送和传输完成会有异步的通知回调
        //设置转换器 发送bytes数据
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        converter.setPayloadAsBytes(true);
        return messageHandler;
    }


    /**
     * <desc>
     * 通过通道获取数据
     * </desc>
     *
     * @param : []
     * @return : org.springframework.messaging.MessageHandler
     * @author : maju
     * @createDate : 2024/1/9
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")  //异步处理
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
                String payload = message.getPayload().toString();
                log.debug("mqtt message:{},topic:{}", payload, topic);
                List<IMqttHandleMsg> handleMsgList = new ArrayList<>();
                hadleMsgMap.forEach((k, v) -> {
                    if (topic.toString().startsWith(k)) {
                        handleMsgList.addAll(v);
                    }
                });
                if (null == handleMsgList || handleMsgList.isEmpty()) {
                    log.error("未匹配到处理器，topic：{}", topic);
                    return;
                }
                for (IMqttHandleMsg iMqttHandleMsg : handleMsgList) {
                    if (iMqttHandleMsg.match(topic, payload)) {
                        iMqttHandleMsg.handleMsg(topic, payload);
                    }
                }
            }
        };
    }

}
