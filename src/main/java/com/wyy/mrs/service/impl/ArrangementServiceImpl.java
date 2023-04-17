package com.wyy.mrs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyy.mrs.mapper.ArrangementMapper;
import com.wyy.mrs.mapper.FilmMapper;
import com.wyy.mrs.mapper.OrderMapper;
import com.wyy.mrs.model.entity.Arrangement;
import com.wyy.mrs.model.entity.Order;
import com.wyy.mrs.model.vo.ArrangementVO;
import com.wyy.mrs.service.ArrangementService;
import com.wyy.mrs.utils.DataTimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArrangementServiceImpl implements ArrangementService {

    @Resource
    private ArrangementMapper arrangementMapper;

    @Resource
    private FilmMapper filmMapper;

    @Resource
    private OrderMapper orderMapper;

    @Override
    public void save(Arrangement arrangement) {
        arrangement.setBoxOffice(0);
        arrangement.setCreateAt(DataTimeUtil.getNowTimeString());
        arrangementMapper.insert(arrangement);
    }

    @Override
    public List<Arrangement> findAll() {
        return arrangementMapper.selectList(null);
    }

    @Override
    public ArrangementVO findByFilmId(String fid) {
        List<Arrangement> list = arrangementMapper.selectList(new QueryWrapper<Arrangement>().in("fid", fid));
        return new ArrangementVO(list, filmMapper.selectById(fid));
    }

    @Override
    public List<Integer> getSeatsHaveSelected(String id) {
        List<Order> orders = orderMapper.selectList(new QueryWrapper<Order>().in("aid", id));
        List<Integer> seats = new ArrayList<>();
        for (Order o : orders) {
            String[] split = o.getSeats().split("号");
            for (String s : split) {
                seats.add(Integer.parseInt(s));
            }
        }
        return seats;
    }

    @Override
    public Arrangement findById(String id) {
        return arrangementMapper.selectById(id);
    }

    @Override
    public void deleteById(String id) {
        arrangementMapper.deleteById(id);
    }

    @Override
    public Arrangement Update(Arrangement arrangement) {
        arrangementMapper.updateById(arrangement);
        return arrangement;
    }

}
