package com.commtalk.domain.member.service.impl;

import com.commtalk.domain.member.dto.MemberDTO;
import com.commtalk.domain.member.dto.MemberUpdateDTO;
import com.commtalk.domain.member.entity.AccountRole;
import com.commtalk.domain.member.exception.*;
import com.commtalk.domain.member.repository.AccountRoleRepository;
import com.commtalk.security.JwtAuthenticationProvider;
import com.commtalk.domain.member.dto.JoinDTO;
import com.commtalk.domain.member.dto.LoginDTO;
import com.commtalk.domain.member.entity.Account;
import com.commtalk.domain.member.repository.AccountRepository;
import com.commtalk.domain.member.service.MemberService;
import com.commtalk.domain.member.entity.Member;
import com.commtalk.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final AccountRepository accountRepo;
    private final AccountRoleRepository accountRoleRepo;
    private final MemberRepository memberRepo;

    @Override
    public String login(LoginDTO loginDto) {
        // 회원 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getNickname(), loginDto.getPassword())
        );

        // JWT 생성
        return jwtAuthenticationProvider.generateToken(authentication);
    }

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
        AccountRole role = accountRoleRepo.findByRoleName(AccountRole.Role.ROLE_USER)
                .orElseThrow(() -> new AccountRoleNotFoundException("사용자 계정 권한을 찾을 수 없습니다."));
        Account account = Account.create(joinDto, memberId, role, passwordEncoder);
        accountRepo.save(account);

        return memberId;
    }

    @Override
    public MemberDTO getInfoById(Long memberId) {
        // 회원 조회
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
        // 회원 계정 조회
        Account account = accountRepo.findByMemberId(member.getId())
                .orElseThrow(() -> new AccountNotFoundException("사용자 계정을 찾을 수 없습니다."));

        return MemberDTO.from(member, account);
    }

    @Override
    @Transactional
    public void updateInfo(Long memberId, MemberUpdateDTO updateDto) {
        // 회원 조회
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        // 회원 정보 수정
        member.setMemberName(updateDto.getUsername());
        member.setEmail(updateDto.getEmail());
        member.setPhone(updateDto.getPhone());

        // 수정된 회원 정보 저장
        memberRepo.save(member);
    }

}
