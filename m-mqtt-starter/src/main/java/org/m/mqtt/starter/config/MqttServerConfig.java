package org.m.mqtt.starter.config;

import org.m.mqtt.starter.server.MqttServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(value = MqttServerProperties.class)
@Configuration
@ComponentScan("org.m.mqtt.starter.server")
public class MqttServerConfig {
}
