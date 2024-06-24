package com.commtalk.config.security;

import com.commtalk.domain.auth.entity.Account;
import com.commtalk.domain.auth.repository.AccountRepository;
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
        Account account = accountRepo.findByNickname(nickname).orElse(null);

        if (account == null || !account.getNickname().equals(nickname)) {
            throw new UsernameNotFoundException("User not found with nickname: " + nickname);
        }

        return new PrincipalDetails(account);
    }
}
