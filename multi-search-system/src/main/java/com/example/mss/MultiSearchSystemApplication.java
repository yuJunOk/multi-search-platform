package com.example.mss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author pengYuJun
 */
@MapperScan("com.example.mss.mapper")
@SpringBootApplication
public class MultiSearchSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiSearchSystemApplication.class, args);
    }

}
