package com.hedon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Hedon Wang
 * @create 2020-10-12 14:37
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //这里表示 username 任意，密码只要是 123456 就可以了。
        return User.withUsername(username)
                .password(passwordEncoder.encode("123456"))
                .authorities("ROLE_USER","ROLE_ORDER","ROLE_PRICE")
                .build();
    }
}
