package org.m.mqtt.starter.annotation;

import org.m.mqtt.starter.config.MqttClientConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MqttClientConfig.class})
public @interface EnableMqttClient {
}
