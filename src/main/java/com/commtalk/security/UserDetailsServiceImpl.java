package com.commtalk.security;

import com.commtalk.domain.member.entity.Account;
import com.commtalk.domain.member.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepo;

    @Override
    public PrincipalDetails loadUserByUsername(String nickname) {
        Account account = accountRepo.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with nickname: " + nickname));
        return new PrincipalDetails(account);
    }
}
