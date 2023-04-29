package com.wyy.mrs;

import com.alibaba.fastjson.JSON;
import com.wyy.mrs.model.entity.Film;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.Serializable;
import java.util.*;


@SpringBootTest(classes = MrsApplication.class)
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
public class MrsApplicationTests {

    public class FilmEvaluate implements Serializable {
        //电影id
        private String fid;
        //用户id
        private String uid;
        //评分
        private Integer star;
        public FilmEvaluate(String uid, String fid, Integer star){
            this.uid = uid;
            this.fid = fid;
            this.star = star;
        }
    }

    @Test
    public void aaa() {

        Map<String, Map<String,Integer>> userFilmSatrt = new HashMap();

        //第一个用户的电影评分
        Map<String,Integer> filmAndSatrt1 = new HashMap();
        for (int i = 0; i < 15; i++) {
            int a = new Random().nextInt(6) ;
            filmAndSatrt1.put("film"+i,a);
        }
        userFilmSatrt.put("user"+"1",filmAndSatrt1);

        //第二个用户的电影评分
        Map<String,Integer> filmAndSatrt2 = new HashMap();
        for (int i = 0; i < 15; i++) {
            int a = new Random().nextInt(6) ;
            filmAndSatrt2.put("film"+i,a);
        }
        userFilmSatrt.put("user"+"2",filmAndSatrt2);

        //第三个用户的电影评分
        Map<String,Integer> filmAndSatrt3 = new HashMap();
        for (int i = 0; i < 15; i++) {
            int a = new Random().nextInt(6) ;
            filmAndSatrt3.put("film"+i,a);
        }
        userFilmSatrt.put("user"+"3",filmAndSatrt3);

        //第四个用户的电影评分
        Map<String,Integer> filmAndSatrt4 = new HashMap();
        for (int i = 0; i < 15; i++) {
            int a = new Random().nextInt(6) ;
            filmAndSatrt4.put("film"+i,a);
        }
        userFilmSatrt.put("user"+"4",filmAndSatrt4);

        //序列化
        System.out.println("hello");
        String s1 = JSON.toJSONString(userFilmSatrt);
        System.out.println(s1);

        //反序列化
        Map<String, Map<String, Integer>> map = JSON.parseObject(s1, Map.class);
        System.out.println(map);
//        System.out.println(map.get("user1"));
//        System.out.println(map.get("user2"));
//        System.out.println(map.get("user3"));
//        System.out.println(map.get("user4"));

        List<FilmEvaluate> list = new ArrayList<>();
        FilmEvaluate f1 = new FilmEvaluate("user1","film1", 5);
        FilmEvaluate f2 = new FilmEvaluate("user1","film2", 4);
        FilmEvaluate f3 = new FilmEvaluate("user1","film3", 3);
        FilmEvaluate f4 = new FilmEvaluate("user2","film1", 5);
        FilmEvaluate f5 = new FilmEvaluate("user2","film2", 4);
        FilmEvaluate f6 = new FilmEvaluate("user2","film3", 3);
        FilmEvaluate f7 = new FilmEvaluate("user3","film1", 5);
        FilmEvaluate f8 = new FilmEvaluate("user3","film2", 4);
        FilmEvaluate f9 = new FilmEvaluate("user3","film3", 2);
        FilmEvaluate f10 = new FilmEvaluate("user4","film1", 5);
        FilmEvaluate f11 = new FilmEvaluate("user4","film2", 2);
        FilmEvaluate f12 = new FilmEvaluate("user4","film3", 1);
        FilmEvaluate f13 = new FilmEvaluate("user5","film1", 2);
        list.add(f1);
        list.add(f2);
        list.add(f3);
        list.add(f4);
        list.add(f5);
        list.add(f6);
        list.add(f7);
        list.add(f8);
        list.add(f9);
        list.add(f10);
        list.add(f11);
        list.add(f12);
        list.add(f13);

    }


}
