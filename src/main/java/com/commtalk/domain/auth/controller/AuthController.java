package com.commtalk.domain.auth.controller;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.auth.dto.JoinDTO;
import com.commtalk.domain.auth.dto.LoginDTO;
import com.commtalk.domain.auth.service.AuthService;
import com.commtalk.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "auth", description = "인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    private final AuthService authSvc;
    private final BoardService boardSvc;

    @Operation(summary = "회원가입")
    @PostMapping(path = "/join")
    public ResponseEntity<ResponseDTO> join(@RequestBody @Valid JoinDTO joinDto) {
        Long memberId = authSvc.join(joinDto); // 회원가입
        boardSvc.pinDefaultBoardByMember(memberId); // 기본 고정 게시판 저장
        return ResponseDTO.of(HttpStatus.CREATED, "회원가입에 성공했습니다.");
    }

    @Operation(summary = "로그인")
    @PostMapping(path = "/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody @Valid LoginDTO loginDto) {
        String token = authSvc.login(loginDto); // 로그인
        return ResponseDTO.of(HttpStatus.OK, token);
    }

}
