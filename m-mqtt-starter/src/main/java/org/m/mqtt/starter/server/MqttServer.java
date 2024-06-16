package org.m.mqtt.starter.server;

import cn.hutool.core.io.FileUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLEngine;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @date 2023/7/1 10:52
 */
@Slf4j
@Component
public class MqttServer {

    @Autowired
    private MqttServerProperties mqttServerProperties;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private SslContext sslContext;

    private Channel tcpChannel;

    private Channel websocketChannel;

    @Autowired
    private MqttMessageHandler mqttMessageHandler;

    @PostConstruct
    public void start() throws Exception {
        log.info("init mqtt server");
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        if (mqttServerProperties.getIsOpenSsl()) {
            InputStream privateKey = null;
            InputStream cert = null;
            if (StringUtils.isEmpty(mqttServerProperties.getSslCertificatePath()) || mqttServerProperties.getSslCertificatePath().contains("classpath")) {
                ClassPathResource certResource = new ClassPathResource(mqttServerProperties.getSslCertificatePath().split(":")[1]);
                ClassPathResource privateResource = new ClassPathResource(mqttServerProperties.getSslCertificateKeyPath().split(":")[1]);
                cert = certResource.getInputStream();
                privateKey = privateResource.getInputStream();
            } else {
                cert = FileUtil.getInputStream(mqttServerProperties.getSslCertificatePath());
                privateKey = FileUtil.getInputStream(mqttServerProperties.getSslCertificateKeyPath());
            }
            sslContext = SslContextBuilder.forServer(
                            cert,
                            privateKey)
                    .build();
        }
        websocketServer();
        log.info("start mqtt server, websocket port: {} ", mqttServerProperties.getWebsocketPort());
    }

    @PreDestroy
    public void stop() {
        log.info("shutdown mqtt server");
        bossGroup.shutdownGracefully();
        bossGroup = null;
        workerGroup.shutdownGracefully();
        workerGroup = null;
        tcpChannel.closeFuture().syncUninterruptibly();
        tcpChannel = null;
        websocketChannel.closeFuture().syncUninterruptibly();
        websocketChannel = null;
        log.info("finish shutdown mqtt server");
    }

    private void websocketServer() throws Exception {
        try {
            new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .localAddress(new InetSocketAddress(mqttServerProperties.getWebsocketPort()))
                    // 配置 编码器、解码器、业务处理
                    .channel(NioServerSocketChannel.class)
                    //ChannelOption.SO_BACKLOG对应的是tcp/ip协议listen函数中的backlog参数，
                    // 函数listen(int socketfd,int backlog)用来初始化服务端可连接队列，
                    // 服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，
                    // 多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //快速复用,防止服务端重启端口被占用的情况发生
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            configSsl(socketChannel, channelPipeline);
                            // 心跳时间
                            channelPipeline.addLast("idle", new IdleStateHandler(600, 600, 1200, TimeUnit.SECONDS));
                            channelPipeline.addLast("encoder", MqttEncoder.INSTANCE);
                            channelPipeline.addLast("decoder", new MqttDecoder());
                            channelPipeline.addLast("mqttMessageHandler", mqttMessageHandler);
                        }
                    })
                    //如果TCP_NODELAY没有设置为true,那么底层的TCP为了能减少交互次数,会将网络数据积累到一定的数量后,
                    // 服务器端才发送出去,会造成一定的延迟。在互联网应用中,通常希望服务是低延迟的,建议将TCP_NODELAY设置为true
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //默认的心跳间隔是7200s即2小时。Netty默认关闭该功能。
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .bind().sync();
            log.info("MQTT服务启动成功！开始监听端口：{}", mqttServerProperties.getWebsocketPort());
        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    private void configSsl(SocketChannel socketChannel, ChannelPipeline channelPipeline) {
        if (mqttServerProperties.getIsOpenSsl()) {
            SSLEngine sslEngine = sslContext.newEngine(socketChannel.alloc());
            // 服务端模式
            sslEngine.setUseClientMode(false);
            // 不需要验证客户端
            sslEngine.setNeedClientAuth(false);
            channelPipeline.addLast("ssl", new SslHandler(sslEngine));
        }
    }
}
