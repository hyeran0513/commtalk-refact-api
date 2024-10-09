package com.commtalk.domain.member.controller;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.member.dto.request.*;
import com.commtalk.domain.member.dto.MemberDTO;
import com.commtalk.domain.member.service.MemberService;
import com.commtalk.domain.board.service.BoardService;
import com.commtalk.security.JwtAuthenticationProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Tag(name = "member", description = "회원 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/members")
public class MemberController {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final MemberService memberSvc;
    private final BoardService boardSvc;

    @Operation(summary = "로그인")
    @PostMapping(path = "/login")
    public ResponseEntity<ResponseDTO<String>> login(@RequestBody @Valid MemberLoginRequest loginReq) {
        String token = memberSvc.login(loginReq); // 로그인
        return ResponseDTO.of(HttpStatus.OK, token);
    }

    @Operation(summary = "토큰 유효성 검사")
    @PostMapping(path = "/token/validate")
    public ResponseEntity<ResponseDTO<String>> validate() {
        return ResponseDTO.of(HttpStatus.OK, "유효한 토큰입니다.");
    }

    @Operation(summary = "회원 생성")
    @PostMapping(path = "")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseDTO<String>> createMember(@RequestBody @Valid MemberJoinRequest joinReq) {
        Long memberId = memberSvc.join(joinReq); // 회원가입
        boardSvc.pinDefaultBoard(memberId); // 기본 고정 게시판 저장
        return ResponseDTO.of(HttpStatus.CREATED, "회원가입에 성공했습니다.");
    }

    @Operation(summary = "회원 조회")
    @GetMapping(path = "/me")
    public ResponseEntity<MemberDTO> getMyInfo(HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        MemberDTO memberDto = memberSvc.getInfoById(memberId); // 회원 조회
        return ResponseEntity.ok(memberDto);
    }

    @Operation(summary = "회원 수정")
    @PatchMapping(path = "/me")
    public ResponseEntity<MemberDTO> updateMyInfo(@RequestBody @Valid MemberUpdateRequest updateReq,
                                                  HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        memberSvc.updateInfo(memberId, updateReq); // 회원 정보 수정

        MemberDTO updateMemberDto = memberSvc.getInfoById(memberId); // 수정된 회원 조회
        return ResponseEntity.ok(updateMemberDto);
    }

    @Operation(summary = "회원 삭제")
    @DeleteMapping(path = "/me")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseDTO<String>> deleteMember(HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        memberSvc.withdraw(memberId); // 회원탈퇴
        return ResponseDTO.of(HttpStatus.CREATED, "회원탈퇴에 성공했습니다.");
    }

    @Operation(summary = "비밀번호 변경")
    @PatchMapping(path = "/password")
    public ResponseEntity<ResponseDTO<String>> updatePassword(@RequestBody @Valid MemberPasswordUpdateRequest updateReq,
                                                              HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        memberSvc.confirmPassword(memberId, updateReq.getCurrentPassword()); // 현재 비밀번호 확인
        memberSvc.updatePassword(memberId, updateReq);  // 비밀번호 변경
        return ResponseDTO.of(HttpStatus.OK, "비밀번호 변경에 성공했습니다.");
    }

    @Operation(summary = "현재 비밀번호 확인")
    @PostMapping(path = "/password/confirm")
    public ResponseEntity<ResponseDTO<String>> confirmPassword(@RequestBody @Valid MemberPasswordConfirmRequest confirmReq,
                                                               HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        memberSvc.confirmPassword(memberId, confirmReq.getCurrentPassword()); // 현재 비밀번호 확인
        return ResponseDTO.of(HttpStatus.OK, "비밀번호 확인이 완료되었습니다.");
    }

}
