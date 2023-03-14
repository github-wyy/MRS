package com.wyy.mrs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wyy
 */

@SpringBootApplication
@MapperScan("com.wyy.mrs.mapper")
public class MrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MrsApplication.class, args);
        LocalDateTime creationTime = LocalDateTime.now();
        String date= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(creationTime);
        System.out.println(date);
    }

}