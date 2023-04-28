package com.wyy.mrs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyy.mrs.mapper.PosterMapper;
import com.wyy.mrs.model.entity.Poster;
import com.wyy.mrs.service.PosterService;
import com.wyy.mrs.utils.DataTimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class PosterServiceImpl implements PosterService {

    @Resource
    private PosterMapper posterMapper;

    @Override
    public void save(Poster poster) {
        poster.setId(UUID.randomUUID().toString());
        poster.setCreateAt(DataTimeUtil.getNowTimeString());
        posterMapper.insert(poster);
    }

    @Override
    public void update(Poster poster) {
        posterMapper.updateById(poster);
    }

    @Override
    public List<Poster> findAll() {
        return posterMapper.selectList(null);
    }

    @Override
    public List<Poster> findByStatus(boolean status) {
        QueryWrapper<Poster> wrapper = new QueryWrapper<>();
        wrapper.in("status", status);
        return posterMapper.selectList(wrapper);
    }

    @Override
    public void deleteById(String id) {
        posterMapper.deleteById(id);
    }

    @Override
    public void deleteAll() {
        posterMapper.delete(null);
    }

}
