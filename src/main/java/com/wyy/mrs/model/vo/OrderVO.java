package com.wyy.mrs.model.vo;

import com.wyy.mrs.model.entity.Arrangement;
import com.wyy.mrs.model.entity.Film;
import com.wyy.mrs.model.entity.Order;
import com.wyy.mrs.model.entity.User;
import lombok.Data;

@Data
public class OrderVO {

    private Order order;

    private User user;

    private Film film;

    private Arrangement arrangement;

}
