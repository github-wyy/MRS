package com.wyy.mrs.config;

import com.wyy.mrs.model.vo.MqQueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列配置
 */
@Configuration
public class RabbitMqConfig {
    /**
     * 交换机
     */
    @Bean
    DirectExchange orderDirect() {
        return ExchangeBuilder
                .directExchange(MqQueueEnum.QUEUE_ORDER.getExchange())
                .durable(true)
                .build();
    }

    /**
     * 订单延迟队列队列所绑定的交换机
     */
    @Bean
    DirectExchange orderTtlDirect() {
        return  ExchangeBuilder
                .directExchange(MqQueueEnum.QUEUE_ORDER_TTL.getExchange())
                .durable(true)
                .build();
    }

    /**
     * 订单实际消费队列
     */
    @Bean
    public Queue orderQueue() {
        return QueueBuilder
                .durable(MqQueueEnum.QUEUE_ORDER.getQueueName())
                .build();
    }

    /**
     * 订单延迟队列（死信队列）
     */
    @Bean
    public Queue orderTtlQueue() {
        return QueueBuilder
                .durable(MqQueueEnum.QUEUE_ORDER_TTL.getQueueName())
                .withArgument("x-dead-letter-exchange", MqQueueEnum.QUEUE_ORDER.getExchange())//到期后转发的交换机
                .withArgument("x-dead-letter-routing-key", MqQueueEnum.QUEUE_ORDER.getRouteKey())//到期后转发的路由键
                .build();
    }

    /**
     * 将队列绑定到交换机
     */
    @Bean
    Binding orderBinding(DirectExchange orderDirect, Queue orderQueue) {
        return BindingBuilder
                .bind(orderQueue)
                .to(orderDirect)
                .with(MqQueueEnum.QUEUE_ORDER.getRouteKey());
    }

    /**
     * 将订单延迟队列绑定到交换机
     */
    @Bean
    Binding orderTtlBinding(DirectExchange orderTtlDirect,Queue orderTtlQueue){
        return BindingBuilder
                .bind(orderTtlQueue)
                .to(orderTtlDirect)
                .with(MqQueueEnum.QUEUE_ORDER_TTL.getRouteKey());
    }

}
