package org.m.mqtt.starter.config;

import org.m.mqtt.starter.client.MqttClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(value = MqttClientProperties.class)
@Configuration
@ComponentScan("org.m.mqtt.starter.client")
public class MqttClientConfig {
}
