package com.wyy.mrs.service;

import com.wyy.mrs.model.entity.Cart;
import com.wyy.mrs.model.entity.Order;
import com.wyy.mrs.model.vo.OrderVO;

import java.util.List;

public interface OrderService {

    Order create(Cart cart) throws Exception;

    Order pay(String id) throws Exception;

    void update(Order order);

    List<OrderVO> findAll();

    List<OrderVO> findByUser(String uid);

}
