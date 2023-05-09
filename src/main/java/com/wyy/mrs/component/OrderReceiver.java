package com.wyy.mrs.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wyy.mrs.mapper.OrderMapper;
import com.wyy.mrs.model.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息消费者
 */
@Component
@RabbitListener(queues = "mrs.order.queue")
public class OrderReceiver {

    @Autowired
    private OrderMapper orderMapper;

    @RabbitHandler
    public void handle(String msg){

        Order order = orderMapper.selectById(msg);
        if (order.getPayAt() == null || order.getPayAt().isEmpty()) {
            orderMapper.delete(
                    new QueryWrapper<Order>().eq("id",msg)
            );
            System.out.printf("receive message record:%s\n",msg);
        }
    }

}
