package com.wyy.mrs.component;

import com.wyy.mrs.model.vo.MqQueueEnum;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息发送者
 */
@Component
public class OrderSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送消息
     */
    public void sendMessage(String orderId, final long delayTimes) {
        String msg = orderId;
        /*try {   // 对Jackson操作异常处理
            msg = objectMapper.writeValueAsString(orderId); //将Record对象转换为json字符串
        }catch (Exception e){
            e.printStackTrace();
        }*/

        /**
         * 发送消息
         */
        amqpTemplate.convertAndSend(MqQueueEnum.QUEUE_ORDER_TTL.getExchange(), // 发送到那个交换机
                MqQueueEnum.QUEUE_ORDER_TTL.getRouteKey(), // 指定路由键
                msg,
                message -> {
                    //给消息设置延迟毫秒值
                    message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
                    return message;
                });

        System.out.printf("send message record:%s\n",msg);
    }
}
