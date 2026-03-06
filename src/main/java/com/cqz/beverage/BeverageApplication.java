package com.cqz.beverage;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cqz.beverage.mapper")
public class BeverageApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeverageApplication.class, args);
    }

}
