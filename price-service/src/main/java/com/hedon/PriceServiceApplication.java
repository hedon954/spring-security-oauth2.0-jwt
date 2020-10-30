package com.hedon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author Hedon Wang
 * @create 2020-10-12 08:38
 */
@SpringBootApplication
@EnableResourceServer  //资源服务器
public class PriceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PriceServiceApplication.class,args);
    }
}
