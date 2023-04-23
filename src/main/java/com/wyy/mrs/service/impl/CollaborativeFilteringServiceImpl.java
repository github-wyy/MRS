package com.wyy.mrs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wyy.mrs.mapper.CollaborativeFilteringMapper;
import com.wyy.mrs.mapper.FilmMapper;
import com.wyy.mrs.mapper.UserMapper;
import com.wyy.mrs.model.entity.CollaborativeFiltering;
import com.wyy.mrs.model.entity.Film;
import com.wyy.mrs.model.entity.FilmEvaluate;
import com.wyy.mrs.model.entity.User;
import com.wyy.mrs.service.CollaborativeFilteringService;
import com.wyy.mrs.utils.RecommendUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CollaborativeFilteringServiceImpl implements CollaborativeFilteringService{

    @Resource
    CollaborativeFilteringMapper collaborativeFilteringMapper;
    @Resource
    FilmMapper filmMapper;
    @Resource
    UserMapper userMapper;


    //TODO test
    @Override
    public List<Film> findFilmBaseUserFiler(String uid) {
        //新用户无评分，使用热度推荐 user表增加标志位
        User user = userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("id",uid)
        );
        //表示user没有评价
        if (user.getIsEvaluate().equals(0)) {
            List<Film> topTenFilms = filmMapper.selectList(
                    new QueryWrapper<Film>()
                            .orderByDesc("hot")
                            .last("LIMIT 10")
            );
            return topTenFilms;
        }

        //该用户评价过电影，协同过滤推荐
        CollaborativeFiltering one = collaborativeFilteringMapper.selectOne(
                new QueryWrapper<CollaborativeFiltering>()
                        .eq("cname", "cf")
        );
        String value = one.getValue();
        //对 one 反序列化 为 map[String]map[String]Integer
        Map<String, Map<String, Integer>> map = JSON.parseObject(value, Map.class);
        //1、计算该用户与其他用户的相似度 计算两个用户的评分向量的余弦夹角作为相似度
        //2、计算该用户的前15个最近邻  对相似度进行排序，取前十五个//最少需要两个
        //3、预测评分 （每个最近邻对每个项目的评分*相似度）之和 除以最近邻数
        //4、对预测评分排序，推荐,返回电影列表
        List<String> list = RecommendUtil.recommendMovies(uid,map);
        List<Film> movies = getMovies(list);

        //应该返回电影列表
        return movies;
    }

    //TODO test
    @Override
    public void updateValue(List<FilmEvaluate> list) {
        //1.对list进行for循环，将uid，fid，start存入 map[string]map[string]string
        System.out.println(list);
        Map<String, Map<String,Integer>> userFilmSatrt = new HashMap();
        Map<String,Integer> filmAndSatrt = new HashMap();
        for (FilmEvaluate item: list) {
            filmAndSatrt.put(item.getFid(),item.getStar());
            userFilmSatrt.put(item.getUid(),filmAndSatrt);
        }
        System.out.println(userFilmSatrt);

        //2.将map序列化为json string，存入表CollaborativeFiltering（value String）
        String value = JSON.toJSONString(userFilmSatrt);
        System.out.println(value);
        //entity
        CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering();
        collaborativeFiltering.setCname("cf");
        collaborativeFiltering.setValue(value);
        //更新数据
        collaborativeFilteringMapper.update(collaborativeFiltering,
                new QueryWrapper<CollaborativeFiltering>()
                        .eq("cname","cf")
                );
        System.out.println("CollaborativeFiltering更新...");
    }

    //返回film列表
    public List<Film> getMovies(List<String> list) {
        ArrayList<Film> films = new ArrayList<>();
        for (String item: list) {
            Film film = filmMapper.selectOne(
                    new QueryWrapper<Film>()
                            .eq("cname","cf")
            );
            films.add(film);
        }
        return films;
    }
}
