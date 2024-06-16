package org.m.mqtt.starter.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <desc>
 * mqtt配置类
 * </desc>
 *
 * @author maju
 * @createDate 2024/1/9
 */
@Component
@ConfigurationProperties(prefix = "mqtt.client")
@Data
public class MqttClientProperties {
    private String url;
    private String clientId;
    private String clientIdOut;
    private String topics;
    private String ackTopics;
    private String username;
    private String password;
    private String clientCrtPath;

    public String getAckTopics() {
        return topics + "/ack";
    }

    public String getClientIdOut() {
        return clientId + "/out";
    }

    public void format(String projectId, String sn, Integer cardType) {
        this.setClientId(String.format(this.getClientId(), projectId, sn, cardType));
        this.setTopics(String.format(this.getTopics(), projectId, sn));
        this.setUsername(String.format(this.getUsername(), projectId, sn));
    }
}
