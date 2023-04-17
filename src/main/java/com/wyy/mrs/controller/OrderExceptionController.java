package com.wyy.mrs.controller;

import com.wyy.mrs.model.entity.OrderException;
import com.wyy.mrs.service.OrderExceptionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
//订单异常上报接口
@RequestMapping("/api/oe")
public class OrderExceptionController {

    @Resource
    private OrderExceptionService orderExceptionService;

    @PostMapping("")
    //添加异常订单
    public OrderException create(@RequestBody OrderException orderException) {
        return orderExceptionService.create(orderException);
    }

    @GetMapping("")
    //查询所有异常订单
    public List<OrderException> findAll() {
        return orderExceptionService.findAll();
    }

    @PutMapping("")
    //工作人员处理异常订单
    public void handle(@RequestBody OrderException orderException) {
        orderExceptionService.handleException(orderException);
    }

}
