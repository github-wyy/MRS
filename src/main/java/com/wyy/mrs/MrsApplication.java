package com.wyy.mrs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wyy.mrs.mapper")
public class MrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MrsApplication.class, args);
        System.out.println("run..........");
    }

}