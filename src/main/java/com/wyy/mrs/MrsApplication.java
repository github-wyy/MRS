package com.wyy.mrs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wyy
 */

@SpringBootApplication
@MapperScan("com.wyy.mrs.mapper")
public class MrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MrsApplication.class, args);
        //Map<String, Map<String,String>> map = new HashMap<>();
        System.out.println("run..........");
    }

}