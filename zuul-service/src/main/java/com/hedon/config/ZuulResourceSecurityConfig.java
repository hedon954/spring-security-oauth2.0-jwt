package com.hedon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author Hedon Wang
 * @create 2020-10-31 01:08
 */
@Configuration
@EnableResourceServer  //作为资源服务器
public class ZuulResourceSecurityConfig extends ResourceServerConfigurerAdapter {

    /**
     * 配置资源服务器
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("zuul-service");
    }

    /**
     * 访问权限控制
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/token/**").permitAll()  //放行所有以 /token 开头的请求
                .anyRequest().authenticated();
    }
}
