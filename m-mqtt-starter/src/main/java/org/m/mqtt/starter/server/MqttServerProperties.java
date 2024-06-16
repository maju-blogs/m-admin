package org.m.mqtt.starter.server;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @date 2023/7/1 11:26
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mqtt.server")
public class MqttServerProperties {
    /**
     * 是否开启ssl
     */
    private Boolean isOpenSsl = false;

    /**
     * SSL证书文件的绝对路径，只支持pfx格式的证书
     */
    private String sslCertificatePath;

    /**
     * 服务器秘钥
     */
    private String sslCertificateKeyPath;

    /**
     * websocket端口
     */
    private Integer websocketPort = 8082;
    /**
     * 等待时间
     */
    private Integer waitTime = 10000;

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

}
