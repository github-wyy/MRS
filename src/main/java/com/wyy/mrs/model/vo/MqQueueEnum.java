package com.wyy.mrs.model.vo;

import lombok.Getter;

/**
 * 消息队列的枚举配置类
 * 用于消息队列及处理更新记录消息队列的常量定义，包括交换机名称、队列名称、路由键名称。
 */
@Getter
public enum MqQueueEnum {

    QUEUE_ORDER("mrs.order.direct","mrs.order.queue","mrs.order.routeKey"),

    QUEUE_ORDER_TTL("mrs.order.direct.ttl", "mrs.order.direct.ttl", "mrs.order.direct.ttl");

    /**
     * 交换机名称
     */
    private String exchange;

    /**
     * 队列名称
     */
    private String queueName;

    /**
     * 路由键
     */
    private String routeKey;

    MqQueueEnum(String exchange, String queueName, String routeKey) {
        this.exchange = exchange;
        this.queueName = queueName;
        this.routeKey = routeKey;
    }

}
