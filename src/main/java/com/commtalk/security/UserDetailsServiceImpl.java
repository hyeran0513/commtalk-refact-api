package com.commtalk.security;

import com.commtalk.domain.member.entity.Member;
import com.commtalk.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepo;

    @Override
    public PrincipalDetails loadUserByUsername(String nickname) {
        Member member = memberRepo.findByNickName(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with nickname: " + nickname));
        return new PrincipalDetails(member);
    }
}
