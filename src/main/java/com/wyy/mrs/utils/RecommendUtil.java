package com.wyy.mrs.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wyy
 */
public final class RecommendUtil {

    // 首先，我们需要根据用户的评分计算用户之间的相似性。使用修正的余弦相似性，我
    // 们将创建一个嵌套循环来迭代每对用户，并计算他们的相似性。我们将相似性得分存储
    // 在名为userSimilarities的Map<String，Map<String，Double >中
    public static Map<String, Map<String, Double>> getUserSimilarities(Map<String, Map<String, Integer>> userRatings) {
        Map<String, Map<String, Double>> userSimilarities = new HashMap<>();
        for (String user1 : userRatings.keySet()) {
            for (String user2 : userRatings.keySet()) {
                if (!user1.equals(user2)) {
                    Map<String, Integer> ratings1 = userRatings.get(user1);
                    Map<String, Integer> ratings2 = userRatings.get(user2);
                    double dotProduct = 0.0;
                    double norm1 = 0.0;
                    double norm2 = 0.0;
                    for (String movie : ratings1.keySet()) {
                        if (ratings2.containsKey(movie)) {
                            dotProduct += (ratings1.get(movie) - 2.5) * (ratings2.get(movie) - 2.5);
                        }
                        norm1 += Math.pow(ratings1.get(movie) - 2.5, 2);
                    }
                    for (String movie : ratings2.keySet()) {
                        norm2 += Math.pow(ratings2.get(movie) - 2.5, 2);
                    }
                    double similarity = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
                    if (similarity > 0.5) {
                        if (!userSimilarities.containsKey(user1)) {
                            userSimilarities.put(user1, new HashMap<String, Double>());
                        }
                        userSimilarities.get(user1).put(user2, similarity);
                    }
                }
            }
        }
        return userSimilarities;
    }

    // 现在我们有了用户相似性，我们可以使用它们向给定用户推荐电影。我们将创建一个名为“推荐的电影”的方法，
    // 该方法接受用户的评分和用户相似性，并返回推荐的电影列表。我们将迭代用户未评分的所有电影，并计算相似
    // 用户评分的加权平均值。我们将在名为“电影”的Map<String，Double >中存储加权平均分数。然后，我们将
    // 按分数对电影进行排序，并返回前10部电影
    public static List<String> recommendMovies(String uid, Map<String, Map<String, Integer>> userRatingss) {
        //Map<String, Integer> userRatings
        //Map<String, Map<String, Integer>> userRatingss
        Map<String, Integer> userRatings = userRatingss.get(uid);
        Map<String, Map<String, Double>> userSimilarities = getUserSimilarities(userRatingss);
        Map<String, Double> movieScores = new HashMap<>();
        for (String movie : userRatings.keySet()) {
            if (userRatings.get(movie) == 0) {
                double weightedSum = 0.0;
                double similaritySum = 0.0;
                for (String user : userSimilarities.keySet()) {
                    if (userRatings.containsKey(movie) && userRatings.get(movie) > 0) {
                        double similarity = userSimilarities.get(user).getOrDefault(userRatings, 0.0);
                        weightedSum += similarity * (userRatings.get(movie) - 2.5);
                        similaritySum += similarity;
                    }
                }
                if (similaritySum > 0) {
                    movieScores.put(movie, weightedSum / similaritySum + 2.5);
                }
            }
        }
        List<String> recommendedMovies = new ArrayList<>(movieScores.keySet());
        recommendedMovies.sort((m1, m2) -> Double.compare(movieScores.get(m2), movieScores.get(m1)));
        System.out.println(recommendedMovies);
        return recommendedMovies.subList(0, Math.min(10, recommendedMovies.size()));
    }
}
