package com.wyy.mrs.model.vo;

import com.wyy.mrs.model.entity.Arrangement;
import com.wyy.mrs.model.entity.Cart;
import com.wyy.mrs.model.entity.Film;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购物车前端展示
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartVO {

    private Film film;

    private Arrangement arrangement;

    private Cart cart;

}
