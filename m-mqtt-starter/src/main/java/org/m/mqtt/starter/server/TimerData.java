package org.m.mqtt.starter.server;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @description: 轮询线程信息存储
 **/
public class TimerData {


    public static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(10);

    public static ConcurrentHashMap<Integer, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>();
}

