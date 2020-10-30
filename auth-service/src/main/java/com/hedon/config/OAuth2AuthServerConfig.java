package com.hedon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import javax.sql.DataSource;

/**
 * 认证服务器配置类
 *
 * @author Hedon Wang
 * @create 2020-10-12 09:15
 */
@Configuration
@EnableAuthorizationServer  //认证服务器
@EnableJdbcHttpSession      // session 持久化
public class OAuth2AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    //注入默认的数据源
    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    //token 存储器
    @Bean
    public TokenStore tokenStore(){
//        return new JdbcTokenStore(dataSource);
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    //JWT token 增强器
    @Bean
    public JwtAccessTokenConverter jwtTokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("hedon");  //设置签名
        return converter;
    }

    //配置客户端应用的相关信息
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    //配置令牌管理器
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //这部分由 authenticationManager 来管理
        endpoints
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore())          //配置令牌存储模式
                .tokenEnhancer(jwtTokenEnhancer()) //配置 jwt 令牌增强器
                .authenticationManager(authenticationManager);
    }

    //配置谁能来验 token （有一些请求连验 token 的资格都没有）
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //暴露获取 signingkey 的服务
                .tokenKeyAccess("isAuthenticated()")
                //必须带身份信息来验证token才进行验证
                .checkTokenAccess("isAuthenticated()");
    }
}
