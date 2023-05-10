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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CollaborativeFilteringServiceImpl implements CollaborativeFilteringService{

    @Resource
    CollaborativeFilteringMapper collaborativeFilteringMapper;
    @Resource
    FilmMapper filmMapper;
    @Resource
    UserMapper userMapper;


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
        System.out.printf("2、从CollaborativeFiltering获取value数据：%s\n", value);
        //对 one 反序列化 为 map[String]map[String]Integer
        Map<String, Map<String, Integer>> map = JSON.parseObject(value, Map.class);
        System.out.printf("3、将value数据反序列化为map：\n");
        System.out.println(map);
        //1、计算该用户与其他用户的相似度 计算两个用户的评分向量的余弦夹角作为相似度
        //2、对相似度进行排序，
        //3、预测评分

        //4、拿到film的avgRating的map
//        List<Film> films = filmMapper.selectList(null);
//        Map<String,Double> avgRatingMap = new HashMap();
//        for (Film film : films) {
//            avgRatingMap.put(film.getId(),film.getAvgrating());
//        }
        //5、对预测评分排序，推荐,返回电影列表
        System.out.printf("4、开始执行recommendMovies方法\n");
        //List<String> list = recommendMovies(map,uid);
        List<String> list = recommendFilms(map,uid);
        //List<String> list = recommendMovies(map,uid,avgRatingMap);

        System.out.printf("推荐的电影集合:");
        System.out.println(list);

        //获取电影列表，并返回
        List<Film> movies = getMovies(list);

        //应该返回电影列表
        return movies;
    }

    @Override
    public void updateValue(List<FilmEvaluate> list) {

        //1.对list进行for循环，将uid，fid，start存入 map[string]map[string]string
        //System.out.println(list);
        Map<String, Map<String,Integer>> userFilmSatrt = new HashMap();
        Map<String,Integer> filmAndSatrt = new HashMap();
        for(FilmEvaluate filmEvaluate : list){
            String uid = filmEvaluate.getUid();
            String fid = filmEvaluate.getFid();
            Integer star = filmEvaluate.getStar();
            if(userFilmSatrt.containsKey(uid)){
                Map<String,Integer> filmAndStar = userFilmSatrt.get(uid);
                filmAndStar.put(fid, star);
            }else{
                Map<String,Integer> filmAndStar = new HashMap<>();
                filmAndStar.put(fid, star);
                userFilmSatrt.put(uid, filmAndStar);
            }
        }

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

    public List<String> recommendFilms(Map<String, Map<String, Integer>> userRatings, String uid) {
        // 首先，找到目标用户的评分向量
        Map<String, Integer> targetRatings = userRatings.get(uid);
        Set<String> targetMovies = targetRatings.keySet();

        // 计算用户之间的余弦相似度
        Map<String, Double> similarityScores = new HashMap<>();
        for (String user : userRatings.keySet()) {
            if (!user.equals(uid)) {
                Map<String, Integer> neighborRatings = userRatings.get(user);
                Set<String> neighborMovies = neighborRatings.keySet();

                // 计算目标评分和邻居评分的点积
                double dotProduct = 0.0;
                for (String movie : targetMovies) {
                    if (neighborMovies.contains(movie)) {
                        int targetRating = targetRatings.get(movie);
                        int neighborRating = neighborRatings.get(movie);
                        dotProduct += targetRating * neighborRating;
                    }
                }

                // 计算目标和邻居的相似度
                double targetMagnitude = 0.0;
                double neighborMagnitude = 0.0;
                for (String movie : targetMovies) {
                    targetMagnitude += targetRatings.get(movie) * targetRatings.get(movie);
                }
                for (String movie : neighborMovies) {
                    neighborMagnitude += neighborRatings.get(movie) * neighborRatings.get(movie);
                }
                targetMagnitude = Math.sqrt(targetMagnitude);
                neighborMagnitude = Math.sqrt(neighborMagnitude);

                double cosineSimilarity = dotProduct / (targetMagnitude * neighborMagnitude);
                similarityScores.put(user, cosineSimilarity);
            }
        }

        // 按照相似度递减的方式对邻居进行排序
        List<String> neighbors = new ArrayList<>(similarityScores.keySet());
        Collections.sort(neighbors, (a, b) -> similarityScores.get(b).compareTo(similarityScores.get(a)));
        neighbors = neighbors.subList(0, Math.min(15, neighbors.size())); // 选择前15个邻居

        // 计算所有未看过电影的加权评分
        Map<String, Double> weightedRatings = new HashMap<>();
        Map<String, Double> similaritySums = new HashMap<>();
        for (String neighbor : neighbors) {
            Map<String, Integer> neighborRatings = userRatings.get(neighbor);
            Set<String> neighborMovies = neighborRatings.keySet();
            double neighborSimilarity = similarityScores.get(neighbor);
            for (String movie : neighborMovies) {
                if (!targetMovies.contains(movie)) {
                    double neighborRating = neighborRatings.get(movie);
                    double weightedRating = neighborSimilarity * (neighborRating - getMeanRating(neighborRatings));
                    weightedRatings.put(movie, weightedRatings.getOrDefault(movie, 0.0) + weightedRating);
                    similaritySums.put(movie, similaritySums.getOrDefault(movie, 0.0) + neighborSimilarity);
                }
            }
        }

        // 查找加权评分最高的前10部电影
        List<String> recommendedMovies = new ArrayList<>(weightedRatings.keySet());
        recommendedMovies.removeIf(movie -> targetMovies.contains(movie)); // 删除目标用户看过的电影
        Collections.sort(recommendedMovies, (a, b) -> weightedRatings.get(b).compareTo(weightedRatings.get(a)));
        recommendedMovies = recommendedMovies.subList(0, Math.min(10, recommendedMovies.size())); // 选择前十电影

        return recommendedMovies;
    }

    // 计算平均评分
    private double getMeanRating(Map<String, Integer> ratings) {
        double sum = 0.0;
        for (int rating : ratings.values()) {
            sum += rating;
        }
        return sum / (double) ratings.size();
    }

    // 返回film列表
    public List<Film> getMovies(List<String> list) {
        List<Film> films = new ArrayList<>();
        for (String item: list) {
            Film film = filmMapper.selectOne(
                    new QueryWrapper<Film>()
                            .eq("id",item)
            );
            films.add(film);
        }
        return films;
    }

}
