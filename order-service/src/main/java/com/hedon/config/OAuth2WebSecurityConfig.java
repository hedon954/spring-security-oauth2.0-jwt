package com.hedon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.*;

/**
 * @author Hedon Wang
 * @create 2020-10-12 15:29
 */
@Configuration
@EnableWebSecurity
public class OAuth2WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Bean
    public AccessTokenConverter accessTokenConverter(){
        DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
        DefaultUserAuthenticationConverter authenticationConverter = new DefaultUserAuthenticationConverter();
        authenticationConverter.setUserDetailsService(userDetailsService);
        tokenConverter.setUserTokenConverter(authenticationConverter);
        return tokenConverter;
    }

    //配置令牌验证服务
    @Bean
    public ResourceServerTokenServices tokenServices(){
        RemoteTokenServices remote = new RemoteTokenServices();
        remote.setClientId("orderService");                 //客户端ID
        remote.setClientSecret("123456");                   //客户端secret
        remote.setCheckTokenEndpointUrl("http://localhost:9070/oauth/check_token");//配置去哪里验证 token
        remote.setAccessTokenConverter(accessTokenConverter()); //令牌解析器
        return remote;
    }


    //利用上面的服务去验证 token
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        OAuth2AuthenticationManager manager = new OAuth2AuthenticationManager();
        manager.setTokenServices(tokenServices());
        return manager;
    }
}
