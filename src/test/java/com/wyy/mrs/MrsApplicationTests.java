package com.wyy.mrs;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@SpringBootTest(classes = MrsApplication.class)
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
public class MrsApplicationTests {

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
        String s1 = JSON.toJSONString(userFilmSatrt);
        System.out.println(s1);

        //反序列化
        Map<String, Map<String, Integer>> map = JSON.parseObject(s1, Map.class);
        System.out.println(map);
        System.out.println(map.get("user1"));
        System.out.println(map.get("user2"));
        System.out.println(map.get("user3"));
        System.out.println(map.get("user4"));

    }


}
