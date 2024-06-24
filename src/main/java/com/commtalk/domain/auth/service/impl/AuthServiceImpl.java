package com.commtalk.domain.auth.service.impl;

import com.commtalk.domain.auth.dto.JoinDTO;
import com.commtalk.domain.auth.entity.Account;
import com.commtalk.domain.auth.exception.DuplicateNicknameException;
import com.commtalk.domain.auth.exception.MemberIdNullException;
import com.commtalk.domain.auth.repository.AccountRepository;
import com.commtalk.domain.auth.service.AuthService;
import com.commtalk.domain.member.entity.Member;
import com.commtalk.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepo;
    private final MemberRepository memberRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Long join(JoinDTO joinDto) {
        // 계정 중복 여부 확인
        if (accountRepo.existsByNickname(joinDto.getNickname())) {
            throw new DuplicateNicknameException("닉네임이 중복됩니다.");
        }

        // 회원 생성
        Member member = Member.create(joinDto);
        Long memberId = memberRepo.save(member).getId();

        if (memberId == null) {
            throw new MemberIdNullException("멤버 생성에 실패했습니다.");
        }
        
        // 계정 생성
        Account account = Account.create(joinDto, memberId, passwordEncoder);
        accountRepo.save(account);

        return memberId;
    }

}
