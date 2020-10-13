package com.hedon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author Hedon Wang
 * @create 2020-10-12 15:15
 */

@Configuration
@EnableResourceServer //资源服务器
public class OAuth2ResourcesServerConfig extends ResourceServerConfigurerAdapter {

    //配置资源服务器 ID
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("order-service");
    }

    //控制权限 => 默认所有服务都需要携带token
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/haha/haha").permitAll() // "/haha/haha" 不需要认证，直接放行
                .antMatchers(HttpMethod.POST).access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.GET).access("#oauth2.hasScope('read')")
                .anyRequest().authenticated();  //其他请求都必须经过认证
    }
}
