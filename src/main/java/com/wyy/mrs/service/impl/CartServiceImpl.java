package com.wyy.mrs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyy.mrs.mapper.ArrangementMapper;
import com.wyy.mrs.mapper.CartMapper;
import com.wyy.mrs.mapper.FilmMapper;
import com.wyy.mrs.model.entity.Arrangement;
import com.wyy.mrs.model.entity.Cart;
import com.wyy.mrs.model.entity.Film;
import com.wyy.mrs.model.vo.CartVO;
import com.wyy.mrs.service.CartService;
import com.wyy.mrs.service.OrderService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "cart")
public class CartServiceImpl implements CartService {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private OrderService orderService;

    @Resource
    private ArrangementMapper arrangementMapper;

    @Resource
    private FilmMapper filmMapper;

    @Override
    public void save(Cart cart) throws Exception {
        //评分
        cartMapper.insert(cart);
    }

    @Override
    @CacheEvict
    public void deleteById(String id) {
        cartMapper.deleteById(id);
    }

    @Override
    @CacheEvict
    public void deleteAllByUserId(String uid) {
        cartMapper.delete(new QueryWrapper<Cart>().in("uid", uid));
    }

    @Override
    @Cacheable
    public List<CartVO> findAllByUserId(String uid) {
        List<CartVO> result = new ArrayList<>();
        List<Cart> carts = cartMapper.selectList(new QueryWrapper<Cart>().in("uid", uid));
        for (Cart c : carts) {
            Arrangement arrangement = arrangementMapper.selectById(c.getAid());
            Film film = filmMapper.selectById(arrangement.getFid());
            CartVO cartVO = new CartVO(film, arrangement, c);
            result.add(cartVO);
        }
        return result;
    }

    @Override
    public void deleteCarts(List<Cart> carts) {
        for (Cart c : carts) {
            cartMapper.deleteById(c.getId());
        }
    }

    @Override
    public void settleCarts(List<Cart> carts) throws Exception {
        for (Cart c : carts) {
            orderService.create(c);
        }
    }

}
