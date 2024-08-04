package com.commtalk.domain.member.service.impl;

import com.commtalk.common.exception.CustomException;
import com.commtalk.common.exception.EntityNotFoundException;
import com.commtalk.common.exception.ErrorCode;
import com.commtalk.domain.member.dto.MemberDTO;
import com.commtalk.domain.member.dto.request.MemberPasswordUpdateRequest;
import com.commtalk.domain.member.dto.request.MemberUpdateRequest;
import com.commtalk.domain.member.entity.MemberRole;
import com.commtalk.domain.member.exception.*;
import com.commtalk.domain.member.repository.MemberRoleRepository;
import com.commtalk.security.JwtAuthenticationProvider;
import com.commtalk.domain.member.dto.request.MemberJoinRequest;
import com.commtalk.domain.member.dto.request.MemberLoginRequest;
import com.commtalk.domain.member.entity.MemberPassword;
import com.commtalk.domain.member.repository.MemberPasswordRepository;
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

    private final MemberPasswordRepository passwordRepo;
    private final MemberRoleRepository memberRoleRepo;
    private final MemberRepository memberRepo;

    @Override
    public String login(MemberLoginRequest loginReq) {
        // 회원 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReq.getNickname(), loginReq.getPassword())
        );

        // JWT 생성
        return jwtAuthenticationProvider.generateToken(authentication);
    }

    @Override
    @Transactional
    public Long join(MemberJoinRequest joinReq) {
        // 계정 중복 여부 확인
        if (memberRepo.existsByNickname(joinReq.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // 비밀번호 확인
        if (!joinReq.getPassword().equals(joinReq.getConfirmPassword())) {
            throw new CustomException(ErrorCode.MISMATCH_CONFIRM_PASSWORD);
        }

        // 회원 생성
        MemberRole role = memberRoleRepo.findByRoleName(MemberRole.RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("사용자 계정 권한을 찾을 수 없습니다."));
        Member member = Member.create(joinReq, role);
        Member newMember = memberRepo.save(member);
        if (newMember.getId() == null) {
            throw new MemberIdNullException("멤버 생성에 실패했습니다.");
        }
        
        // 회원 패스워드 생성
        MemberPassword password = MemberPassword.create(joinReq, newMember, passwordEncoder);
        passwordRepo.save(password);

        return newMember.getId();
    }

    @Override
    public MemberDTO getInfoById(Long memberId) {
        // 회원 조회
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        return MemberDTO.from(member);
    }

    @Override
    @Transactional
    public void updateInfo(Long memberId, MemberUpdateRequest updateReq) {
        // 회원 조회
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 회원 정보 수정
        member.setMemberName(updateReq.getUsername());
        member.setEmail(updateReq.getEmail());
        member.setPhone(updateReq.getPhone());

        // 수정된 회원 정보 저장
        memberRepo.save(member);
    }

    @Override
    public void updatePassword(Long memberId, MemberPasswordUpdateRequest updateReq) {
        // 회원 비밀번호 조회
        MemberPassword password = passwordRepo.findByMemberId(memberId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 비밀번호를 찾을 수 없습니다."));

        // 현재 비밀번호 확인
        if (!password.isEqualPassword(updateReq.getCurrentPassword(), passwordEncoder)) {
            throw new CustomException(ErrorCode.MISMATCH_CURRENT_PASSWORD);
        }
        // 비밀번호 확인
        if (!updateReq.getNewPassword().equals(updateReq.getConfirmPassword())) {
            throw new CustomException(ErrorCode.MISMATCH_CONFIRM_PASSWORD);
        }

        // 비밀번호 변경
        password.setNewPassword(updateReq.getNewPassword(), passwordEncoder);

        // 변경된 비밀번호 저장
        passwordRepo.save(password);
    }

}
