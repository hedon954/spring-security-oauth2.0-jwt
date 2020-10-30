package com.hedon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author Hedon Wang
 * @create 2020-10-12 08:30
 */
@SpringBootApplication
@EnableResourceServer  //资源服务器
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class,args);
    }

    /**
     * 定义 OAuth2RestTemplate
     * @param resource   资源信息
     * @param context    请求上下文（就是从这个里面拿到 token，并自动帮我们放到 header 中的）
     * @return
     */
    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context){
        return new OAuth2RestTemplate(resource,context);
    }
}
