package com.wyy.mrs.service;

import com.wyy.mrs.model.entity.OrderException;

import java.util.List;

public interface OrderExceptionService {

    OrderException create(OrderException orderException);

    List<OrderException> findAll();

    void handleException(OrderException orderException);

}
