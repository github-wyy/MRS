package com.wyy.mrs.model.vo;

import lombok.Data;

@Data
public class OrderStatusVO {

    //订单支付状态
    private String orderStatusString;

    //该状态的个数
    private Integer num;

}
