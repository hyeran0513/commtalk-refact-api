package com.commtalk.domain.member.controller;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.member.dto.JoinDTO;
import com.commtalk.domain.member.dto.LoginDTO;
import com.commtalk.domain.member.dto.MemberDTO;
import com.commtalk.domain.member.dto.MemberUpdateDTO;
import com.commtalk.domain.member.exception.MemberIdNullException;
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
    public ResponseEntity<ResponseDTO<String>> login(@RequestBody @Valid LoginDTO loginDto) {
        String token = memberSvc.login(loginDto); // 로그인
        return ResponseDTO.of(HttpStatus.OK, token);
    }

    @Operation(summary = "회원 생성")
    @PostMapping(path = "")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseDTO<String>> createMember(@RequestBody @Valid JoinDTO joinDto) {
        Long memberId = memberSvc.join(joinDto); // 회원가입
        boardSvc.pinDefaultBoard(memberId); // 기본 고정 게시판 저장
        return ResponseDTO.of(HttpStatus.CREATED, "회원가입에 성공했습니다.");
    }

    @Operation(summary = "내 정보 조회")
    @GetMapping(path = "/me")
    public ResponseEntity<MemberDTO> getMyInfo(HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        MemberDTO memberDto = memberSvc.getInfoById(memberId); // 회원 조회
        return ResponseEntity.ok(memberDto);
    }

    @Operation(summary = "내 정보 수정")
    @PatchMapping(path = "/me")
    public ResponseEntity<MemberDTO> updateMyInfo(@RequestBody @Valid MemberUpdateDTO updateDto,
                                                  HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        memberSvc.updateInfo(memberId, updateDto); // 회원 정보 수정

        MemberDTO updateMemberDto = memberSvc.getInfoById(memberId); // 수정된 회원 조회
        return ResponseEntity.ok(updateMemberDto);
    }

}
