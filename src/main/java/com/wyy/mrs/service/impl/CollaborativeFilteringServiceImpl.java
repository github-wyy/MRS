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
        List<String> list = recommendMovies(map,uid);
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

    //使用修正的余弦相似度
    public List<String> recommendMovies(Map<String, Map<String, Integer>> userRatings, String uid) {
        Map<String, Double> similarityMap = new HashMap<>();
        Map<String, Integer> targetUserRatings = userRatings.get(uid);
        for (String user : userRatings.keySet()) {
            if (!user.equals(uid)) {
                Map<String, Integer> otherUserRatings = userRatings.get(user);
                double dotProduct = 0.0;
                double normA = 0.0;
                double normB = 0.0;
                for (String movie : targetUserRatings.keySet()) {
                    if (otherUserRatings.containsKey(movie)) {
                        dotProduct += targetUserRatings.get(movie) * otherUserRatings.get(movie);
                    }
                    normA += Math.pow(targetUserRatings.get(movie), 2);
                }
                for (String movie : otherUserRatings.keySet()) {
                    normB += Math.pow(otherUserRatings.get(movie), 2);
                }
                double similarity = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
                similarityMap.put(user, similarity);
            }
        }
        List<String> recommendedMovies = new ArrayList<>();
        List<Map.Entry<String, Double>> sortedSimilarity = similarityMap.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toList());
        for (String movie : userRatings.get(uid).keySet()) {
            for (Map.Entry<String, Double> entry : sortedSimilarity) {
                if (userRatings.get(entry.getKey()).containsKey(movie)) {
                    sortedSimilarity.remove(entry);
                    break;
                }
            }
        }
        for (Map.Entry<String, Double> entry : sortedSimilarity) {
            if (recommendedMovies.size() >= 10) {
                break;
            }
            for (String movie : userRatings.get(entry.getKey()).keySet()) {
                if (!targetUserRatings.containsKey(movie) && !recommendedMovies.contains(movie)) {
                    recommendedMovies.add(movie);
                    if (recommendedMovies.size() >= 10) {
                        break;
                    }
                }
            }
        }
        return recommendedMovies;
    }

    //返回film列表
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

/*    //使用余弦相似度
    public List<String> recommendMovies(Map<String, Map<String, Integer>> userRatings, String uid) {
        Map<String, Integer> currentUserRatings = userRatings.get(uid);
        Map<String, Double> similarityScores = new HashMap<>();
        for (String user : userRatings.keySet()) {
            if (!user.equals(uid)) {
                Map<String, Integer> otherUserRatings = userRatings.get(user);
                double dotProduct = 0.0;
                double magnitudeCurrentUser = 0.0;
                double magnitudeOtherUser = 0.0;
                for (String movie : currentUserRatings.keySet()) {
                    if (otherUserRatings.containsKey(movie)) {
                        dotProduct += currentUserRatings.get(movie) * otherUserRatings.get(movie);
                    }
                    magnitudeCurrentUser += Math.pow(currentUserRatings.get(movie), 2);
                }
                for (String movie : otherUserRatings.keySet()) {
                    magnitudeOtherUser += Math.pow(otherUserRatings.get(movie), 2);
                }
                double magnitudeProduct = Math.sqrt(magnitudeCurrentUser) * Math.sqrt(magnitudeOtherUser);
                if (magnitudeProduct != 0) {
                    double cosineSimilarity = dotProduct / magnitudeProduct;
                    similarityScores.put(user, cosineSimilarity);
                }
            }
        }
        List<String> recommendedMovies = new ArrayList<>();
        Set<String> moviesAlreadyWatched = currentUserRatings.keySet();
        Map<String, Double> sortedSimilarityScores = similarityScores.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (String similarUser : sortedSimilarityScores.keySet()) {
            Map<String, Integer> similarUserRatings = userRatings.get(similarUser);
            for (String movie : similarUserRatings.keySet()) {
                if (!moviesAlreadyWatched.contains(movie)) {
                    recommendedMovies.add(movie);
                    if (recommendedMovies.size() == 10) {
                        return recommendedMovies;
                    }
                }
            }
        }
        return recommendedMovies;
    }*/

/*
    // 使用修正的余弦相似度，一直推荐不出来
    // 首先，需要根据用户的评分计算用户之间的相似性。使用修正的余弦相似性，
    // 将创建一个嵌套循环来迭代每对用户，并计算他们的相似性。将相似性得分存储
    // 在名为userSimilarities的Map<String，Map<String，Double >中
    public Map<String, Map<String, Double>> getUserSimilarities(Map<String, Map<String, Integer>> userRatingss, Map<String,Double> avgRatingMap) {
        Map<String, Map<String, Double>> userSimilarities = new HashMap<>();
        for (String user1 : userRatingss.keySet()) {
            for (String user2 : userRatingss.keySet()) {
                if (!user1.equals(user2)) {
                    Map<String, Integer> ratings1 = userRatingss.get(user1);
                    Map<String, Integer> ratings2 = userRatingss.get(user2);
                    double dotProduct = 0.0;
                    double norm1 = 0.0;
                    double norm2 = 0.0;
                    for (String movie : ratings1.keySet()) {
                        //Double avgRating = this.filmMapper.selectOne(new QueryWrapper<Film>().eq("id", movie)).getAvgrating();
                        if (ratings2.containsKey(movie)) {
                            dotProduct += (ratings1.get(movie) - avgRatingMap.get(movie))
                                    * (ratings2.get(movie) - avgRatingMap.get(movie));
                        }
                        norm1 += Math.pow(ratings1.get(movie) - avgRatingMap.get(movie), 2);
                    }
                    for (String movie : ratings2.keySet()) {
                        norm2 += Math.pow(ratings2.get(movie) - avgRatingMap.get(movie), 2);
                    }
                    double similarity = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
                    if (similarity > 0.0) {
                        if (!userSimilarities.containsKey(user1)) {
                            HashMap<String, Double> userAndSimilarities = new HashMap<>();
                            userAndSimilarities.put(user2,similarity);
                            userSimilarities.put(user1,userAndSimilarities);
                        }
                        userSimilarities.get(user1).put(user2, similarity);
                    }
                }
            }
        }
        System.out.println("用户相似性");
        System.out.println(userSimilarities);
        return userSimilarities;
    }

    // 现在有了用户相似性，可以使用它们向给定用户推荐电影。创建一个名为“推荐的电影”的方法，
    // 该方法接受用户的评分和用户相似性，并返回推荐的电影列表。迭代用户未评分的所有电影，并计算相似
    // 用户评分的加权平均值。在名为“film”的Map<String，Double >中存储加权平均分数。然后，将
    // 按分数对电影进行排序，并返回前10部电影
    public List<String> recommendMovies(Map<String, Map<String, Integer>> userRatingss, String uid, Map<String,Double> avgRatingMap) {
        //Map<String, Integer> userRatings
        //Map<String, Map<String, Integer>> userRatingss
        Map<String, Integer> userRatings = userRatingss.get(uid);
        Map<String, Map<String, Double>> userSimilarities = getUserSimilarities(userRatingss, avgRatingMap);
        Map<String, Double> movieScores = new HashMap<>();
        for (String movie : userRatings.keySet()) {
            if (userRatings.get(movie) == 0) {
                double weightedSum = 0.0;
                double similaritySum = 0.0;
                for (String user : userSimilarities.keySet()) {
                    if (userRatings.containsKey(movie) && userRatings.get(movie) > 0) {
                        double similarity = userSimilarities.get(user).getOrDefault(userRatings, 0.0);
                        weightedSum += similarity * avgRatingMap.get(movie);
                        similaritySum += similarity;
                    }
                }
                if (similaritySum > 0) {
                    movieScores.put(movie, weightedSum / similaritySum + avgRatingMap.get(movie));
                }
            }
        }
        List<String> recommendedMovies = new ArrayList<>(movieScores.keySet());
        recommendedMovies.sort((m1, m2) -> Double.compare(movieScores.get(m2), movieScores.get(m1)));
        System.out.println(recommendedMovies);
        return recommendedMovies.subList(0, Math.min(10, recommendedMovies.size()));
    }*/
}
