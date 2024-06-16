package org.m;

import org.m.mqtt.starter.annotation.EnableMqttClient;
import org.m.mqtt.starter.annotation.EnableMqttServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
@EnableMqttServer
@EnableMqttClient
public class MWebApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        SpringApplication.run(MWebApplication.class, args);
    }

}
