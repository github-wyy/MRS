package com.wyy.mrs.controller;

import com.wyy.mrs.model.entity.Cart;
import com.wyy.mrs.model.entity.Order;
import com.wyy.mrs.model.vo.OrderVO;
import com.wyy.mrs.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
//订单接口
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("")
    //创建订单
    public void save(@RequestBody Cart cart) throws Exception {
        orderService.create(cart);

        //TODO 发送消息，30分钟后没支付，删除订单
    }

    @GetMapping("")
    //查询所有订单
    public List<OrderVO> findAll() {
        return orderService.findAll();
    }

    @PutMapping("")
    //查询所有订单
    public void update(@RequestBody Order order) {
        orderService.update(order);
    }

    @GetMapping("/user/{id}")
    //查询用户订单
    public List<OrderVO> findByUser(@PathVariable String id) {
        return orderService.findByUser(id);
    }

    @GetMapping("/pay")
    //支付订单
    public Order save(String id) throws Exception {
        return orderService.pay(id);
    }

}
