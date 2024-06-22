package com.commtalk.domain.auth;

import com.commtalk.domain.auth.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final Account account;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(account.getRole().name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getNickname();
    }

    public Long getMemberId() {
        return account.getMemberId();
    }

    // 사용자 계정의 만료 여부를 반환 (기본적으로 true)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 사용자 계정의 잠김 여부를 반환 (기본적으로 true)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 사용자 계정의 패스워드 만료 여부를 반환 (기본적으로 true)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 사용자 계정의 활성 여부를 반환 (기본적으로 true)
    @Override
    public boolean isEnabled() {
        return true;
    }

}
