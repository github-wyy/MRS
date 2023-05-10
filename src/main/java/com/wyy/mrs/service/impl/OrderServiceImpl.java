package com.wyy.mrs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyy.mrs.component.OrderSender;
import com.wyy.mrs.constant.OrderStatus;
import com.wyy.mrs.mapper.FilmMapper;
import com.wyy.mrs.mapper.OrderMapper;
import com.wyy.mrs.mapper.UserMapper;
import com.wyy.mrs.model.entity.Arrangement;
import com.wyy.mrs.model.entity.Cart;
import com.wyy.mrs.model.entity.Film;
import com.wyy.mrs.model.entity.Order;
import com.wyy.mrs.model.vo.OrderStatusVO;
import com.wyy.mrs.model.vo.OrderVO;
import com.wyy.mrs.service.ArrangementService;
import com.wyy.mrs.service.OrderService;
import com.wyy.mrs.utils.DataTimeUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = "order")
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private ArrangementService arrangementService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private FilmMapper filmMapper;

    @Resource
    private OrderSender orderSender;

    @Override
    public Order create(Cart cart) throws Exception {
        List<Integer> seats = arrangementService.getSeatsHaveSelected(cart.getAid());
        String[] split = cart.getSeats().split("号");
        for (String s : split) {
            if (seats.contains(Integer.parseInt(s))) throw new Exception("影片在购物车中躺了太长时间了，座位已被其他用户预订并支付了");
        }
        Order order = new Order();
        //生成订单id
        String orderUUID = UUID.randomUUID().toString();
        order.setId(orderUUID);
        //写入用户id
        order.setUid(cart.getUid());
        //写入用户电话
        order.setPhone(cart.getPhone());
        //写入场次id
        order.setAid(cart.getAid());
        //写入座位信息
        order.setStatus(cart.getStatus());
        order.setSeats(cart.getSeats());
        if (cart.getStatus() == 2) order.setPayAt(DataTimeUtil.getNowTimeString());
        order.setPrice(cart.getPrice());
        order.setCreateAt(DataTimeUtil.getNowTimeString());
        orderMapper.insert(order);

        //订了几个座位就添加多少热度
        Film film = filmMapper.selectById(arrangementService.findById(cart.getAid()).getFid());
        film.setHot(film.getHot() + split.length);
        filmMapper.updateById(film);

        //发送消息，30分钟后没支付，删除订单
        long delayTimes = 30 * 60 * 1000;
        orderSender.sendMessage(orderUUID, delayTimes);

        return order;
    }

    @Override
    public Order pay(String id) throws Exception {
        Order order = orderMapper.selectById(id);
        if (order == null) throw new Exception("不存在的订单id");

        if (DataTimeUtil.parseTimeStamp(order.getCreateAt()) + OrderStatus.EXPIRATION_TIME
                < System.currentTimeMillis()) {
            order.setStatus(OrderStatus.PAYMENT_FAILED);
            orderMapper.updateById(order);
            throw new Exception("订单支付超时");
        }

        order.setStatus(OrderStatus.PAYMENT_SUCCESSFUL);
        order.setPayAt(DataTimeUtil.getNowTimeString());
        orderMapper.updateById(order);
        return order;
    }

    @Override
    public void update(Order order) {
        orderMapper.updateById(order);
    }

    @Override
    public List<OrderVO> findAll() {
        return findByWrapper(new QueryWrapper<>());
    }

    @Override
    public List<OrderVO> findByUser(String uid) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.in("uid", uid);
        return findByWrapper(wrapper);
    }

    @Override
    public List<OrderStatusVO> orderStatus() {

        List<OrderStatusVO> orderStatusVOS = new ArrayList<>();

        //等待支付 0
        OrderStatusVO waiting = new OrderStatusVO();
        waiting.setOrderStatusString(OrderStatus.PAYMENT_WAITING_STRING);
        //支付失败 1
        OrderStatusVO paymentFailed = new OrderStatusVO();
        paymentFailed.setOrderStatusString(OrderStatus.PAYMENT_FAILED_STRING);
        //支付成功 2
        OrderStatusVO paymentSuccessful = new OrderStatusVO();
        paymentSuccessful.setOrderStatusString(OrderStatus.PAYMENT_SUCCESSFUL_STRING);
        //已被撤销 3
        OrderStatusVO countermand = new OrderStatusVO();
        countermand.setOrderStatusString(OrderStatus.COUNTERMAND_STRING);


        //查询所有订单
        List<Order> orders = orderMapper.selectList(
                new QueryWrapper<>()
        );

        int waitingNum = 0;
        int paymentFailedNum = 0;
        int paymentSuccessfulNum = 0;
        int countermandNum = 0;
        //迭代
        for (Order item: orders) {
            if (item.getStatus().equals(0)) {
                waitingNum += 1;
            }
            if (item.getStatus().equals(1)) {
                paymentFailedNum += 1;
            }
            if (item.getStatus().equals(2)) {
                paymentSuccessfulNum += 1;
            }
            if (item.getStatus().equals(3)) {
                countermandNum += 1;
            }

        }

        //设置数量
        waiting.setNum(waitingNum);
        paymentFailed.setNum(paymentFailedNum);
        paymentSuccessful.setNum(paymentSuccessfulNum);
        countermand.setNum(countermandNum);

        //加入List
        orderStatusVOS.add(waiting);
        orderStatusVOS.add(paymentFailed);
        orderStatusVOS.add(paymentSuccessful);
        orderStatusVOS.add(countermand);

        System.out.println(111);
        System.out.println(orderStatusVOS);
        return orderStatusVOS;
    }

    private List<OrderVO> findByWrapper(QueryWrapper<Order> wrapper) {
        List<Order> orders = orderMapper.selectList(wrapper);
        List<OrderVO> result = new ArrayList<>();
        for (Order o : orders) {
            OrderVO orderVO = new OrderVO();
            orderVO.setOrder(o);
            orderVO.setUser(userMapper.selectById(o.getUid()));
            Arrangement arrangement = arrangementService.findById(o.getAid()); // 没查到arrangement，导致下面空指针
            if (arrangement != null) {
                orderVO.setArrangement(arrangement);
                orderVO.setFilm(filmMapper.selectById(arrangement.getFid())); // 空指针 方法：数据库去掉第二个订单试一下，因为debug过程中，只有第二个订单空指针
            }
//            orderVO.setArrangement(arrangement);
//            orderVO.setFilm(filmMapper.selectById(arrangement.getFid()));
            result.add(orderVO);
            System.out.println(result);
        }
        return result;
    }

}
