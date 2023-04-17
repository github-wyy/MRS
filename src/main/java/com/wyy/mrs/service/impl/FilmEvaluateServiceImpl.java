package com.wyy.mrs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyy.mrs.mapper.FilmEvaluateMapper;
import com.wyy.mrs.mapper.FilmMapper;
import com.wyy.mrs.mapper.UserMapper;
import com.wyy.mrs.model.entity.CollaborativeFiltering;
import com.wyy.mrs.model.entity.Film;
import com.wyy.mrs.model.entity.FilmEvaluate;
import com.wyy.mrs.model.vo.FilmEvaluateVO;
import com.wyy.mrs.service.FilmEvaluateService;
import com.wyy.mrs.utils.DataTimeUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "filmEvaluate")
public class FilmEvaluateServiceImpl implements FilmEvaluateService {

    @Resource
    private FilmEvaluateMapper filmEvaluateMapper;

    @Resource
    private FilmMapper filmMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public void save(FilmEvaluate filmEvaluate) throws Exception {
        QueryWrapper<FilmEvaluate> wrapper = new QueryWrapper<>();
        wrapper.in("fid", filmEvaluate.getFid());
        wrapper.in("uid", filmEvaluate.getUid());
        FilmEvaluate one = filmEvaluateMapper.selectOne(wrapper);
        if (one != null) {
            throw new Exception("感谢您的参与，但是您已评分请不要重复操作！");
        }
        filmEvaluate.setCreateAt(DataTimeUtil.getNowTimeString());
        filmEvaluateMapper.insert(filmEvaluate);
        //为电影添加热度
        Film film = filmMapper.selectById(filmEvaluate.getFid());
        film.setHot(film.getHot() + 1);
        filmMapper.updateById(film);
    }

    @Override
    public List<FilmEvaluateVO> findAllByFilmId(String fid) {
        List<FilmEvaluateVO> result = new ArrayList<>();
        List<FilmEvaluate> filmEvaluates = filmEvaluateMapper.selectList(new QueryWrapper<FilmEvaluate>().in("fid", fid));
        for (FilmEvaluate f : filmEvaluates) {
            FilmEvaluateVO filmEvaluateVO = new FilmEvaluateVO();
            filmEvaluateVO.setUser(userMapper.selectById(f.getUid()));
            filmEvaluateVO.setFilmEvaluate(f);
            filmEvaluateVO.setId(f.getId());
            result.add(filmEvaluateVO);
        }
        return result;
    }

    @Override
    public void deleteAllByFilmId(String fid) {
        filmEvaluateMapper.delete(new QueryWrapper<FilmEvaluate>().in("fid", fid));
    }

    @Override
    public void deleteById(String id) {
        filmEvaluateMapper.deleteById(id);
    }

    @Override
    public List<FilmEvaluate> findAllFilm() {
        QueryWrapper<FilmEvaluate> queryWrapper = new QueryWrapper<>();
        List<FilmEvaluate> filmEvaluates = filmEvaluateMapper.selectList(queryWrapper);
        return filmEvaluates;
    }

}
