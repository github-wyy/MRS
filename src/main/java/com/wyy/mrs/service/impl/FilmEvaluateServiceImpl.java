package com.wyy.mrs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyy.mrs.mapper.FilmEvaluateMapper;
import com.wyy.mrs.mapper.FilmMapper;
import com.wyy.mrs.mapper.UserMapper;
import com.wyy.mrs.model.entity.CollaborativeFiltering;
import com.wyy.mrs.model.entity.Film;
import com.wyy.mrs.model.entity.FilmEvaluate;
import com.wyy.mrs.model.entity.User;
import com.wyy.mrs.model.vo.FilmEvaluateVO;
import com.wyy.mrs.service.FilmEvaluateService;
import com.wyy.mrs.utils.DataTimeUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    @Override
    public void ModifyAverageFilmRating(String fid) {
        //1.计算fid的均值
        List<FilmEvaluate> evaluates = filmEvaluateMapper.selectList(
                new QueryWrapper<FilmEvaluate>().eq("fid",fid)
        );
        int num = 0;
        Integer score = 0;
        Double avgRating = 0.0d;
        for (FilmEvaluate filmEvaluate: evaluates) {
            num++;
            score += filmEvaluate.getStar();
        }
        double doubleScore = (double)score;
        double doubleNum = num;  //buzhuanyexing
        avgRating = division(doubleScore, doubleNum, 2);

        //2.对film表的fid存入avgRating
        Film one = filmMapper.selectOne(
                new QueryWrapper<Film>().eq("id", fid)
        );
        one.setAvgrating(avgRating);
        filmMapper.updateById(one);
    }

    public double division(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /*@Override
    public void ModifyAllAverageFilmRating(List<Film> films, List<FilmEvaluate> evaluateList) {

        for (Film film:films) {
            String fid = film.getId();
            List<FilmEvaluate> evaluates = null;
            for (FilmEvaluate filmEvaluate: evaluateList) {
                if (filmEvaluate.getFid() == fid) {
                    evaluates.add(filmEvaluate);
                }
            }

            int num = 0;
            Integer score = 0;
            Double avgScore = 0.0d;
            for (FilmEvaluate filmEvaluate: evaluates) {
                num++;
                score += filmEvaluate.getStar();
            }
            double doubleScore = (double)score;
            double doubleNum = num;  //buzhuanyexing
            avgScore = division(doubleScore, doubleNum, 2);

            //2.对user表的uid用户存入avgScore
            film.setAvgrating(avgScore);
            filmMapper.updateById(film);
        }
    }*/


}
