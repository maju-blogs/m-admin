package org.m.mqtt.starter.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledFuture;

/**
 * @description: 判断策略相关消息是否在规定时间段内发送，获取结束状态
 **/
@Slf4j
public class MonitorMsgTime implements Runnable {

    private Integer packetId;
    private MqttMessage mqttMessage;
    private ChannelHandlerContext ctx;

    public MonitorMsgTime(Integer packetId, MqttMessage mqttMessage, ChannelHandlerContext ctx) {
        this.packetId = packetId;
        this.mqttMessage = mqttMessage;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        //注意，整个执行过程中，代码报错，线程就会终止
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        if (ctx != null && ctx.channel().isActive()) {
            log.info("重复发送消息给客户端：" + address.getHostString());
            if (mqttMessage instanceof MqttPublishMessage) {
                //推送的原始消息,每次推送，都需要重新拷贝一份
                try {
                    MqttPublishMessage mqttPublishMessage = (MqttPublishMessage) mqttMessage;
                    ByteBuf byteBuf = mqttPublishMessage.payload();
                    byteBuf.retainedDuplicate();
                    ctx.writeAndFlush(mqttPublishMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            } else {
                //回复的ack类型消息
                ctx.writeAndFlush(mqttMessage);
            }
        } else {
            log.error(address.getHostString() + " 客户端断开，结束重复发送");
            //如果离线了，就不发了
            ScheduledFuture<?> scheduledFuture = TimerData.scheduledFutureMap.remove(packetId);
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
        }
    }
}
