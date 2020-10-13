package com.hedon.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Hedon Wang
 * @create 2020-10-12 15:48
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements UserDetails {

    //我们自己加3个属性
    private Integer id;
    private String username;
    private String password;

    //拥有哪些权限
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN");
    }

    //密码
    @Override
    public String getPassword() {
        return password;
    }

    //用户名
    @Override
    public String getUsername() {
        return username;
    }

    //是否过期 -> 没有过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //是否被锁 -> 没有被锁
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //证书是否过期 -> 没有过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //用户是否可用 -> 可用
    @Override
    public boolean isEnabled() {
        return true;
    }
}
