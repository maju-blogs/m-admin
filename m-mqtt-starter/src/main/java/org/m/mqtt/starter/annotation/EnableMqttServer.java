package org.m.mqtt.starter.annotation;

import org.m.mqtt.starter.config.MqttServerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MqttServerConfig.class})
public @interface EnableMqttServer {
}
