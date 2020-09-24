package com.cas;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @ClassName : CasApplication
 * @Description : 启动类
 * @Author : pw
 * @Date: 2020-09-23 13:57
 */
@SpringBootApplication
@EnableAsync
@MapperScan(basePackages = "com.cas.mapper")
public class CasApplication {
    public static void main(String[] args) {
        SpringApplication.run(CasApplication.class);
    }

}
