package com.wyy.mrs.controller;

import com.wyy.mrs.model.entity.Cart;
import com.wyy.mrs.model.vo.CartVO;
import com.wyy.mrs.service.CartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
//用户购物车接口
@RequestMapping("/api/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @PostMapping
    //添加购物车
    public void save(@RequestBody Cart cart) throws Exception {
        cartService.save(cart);
    }

    @GetMapping("")
    //根据用户id查询购物车
    public List<CartVO> list(@RequestParam String uid) {
        return cartService.findAllByUserId(uid);
    }

    @DeleteMapping("")
    //删除购物车
    public void delete(String id) {
        cartService.deleteById(id);
    }

}
