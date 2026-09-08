package com.shop.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients(basePackages = {"com.shop"})
@ComponentScan(basePackages = {"com.shop"})
public class ShopFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopFileApplication.class, args);
    }

}