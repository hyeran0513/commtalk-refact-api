package com.commtalk.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 기타 서버 에러
    SERVER_INTERNAL_ERROR(500, "서버 내부에서 에러가 발생했습니다."),

    // 회원 에러
    DUPLICATE_NICKNAME(409, "닉네임이 중복됩니다."),
    MISMATCH_CURRENT_PASSWORD(400, "현재 비밀번호가 일치하지 않습니다."),
    MISMATCH_CONFIRM_PASSWORD(400, "비밀번호 확인 값이 일치하지 않습니다."),
    MEMBER_ROLE_NOT_FOUND(500, "회원 계정 권한을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(500, "회원을 찾을 수 없습니다."),
    MEMBER_CREATE_FAILED(500, "회원 생성에 실패했습니다."),
    MEMBER_PASSWORD_NOT_FOUND(500, "회원 비밀번호를 찾을 수 없습니다."),
    INVALID_MEMBER(500, "유효하지 않은 회원 식별자입니다."),

    // 게시판 에러
    EXCEEDED_PIN_LIMIT(400, "핀 고정 게시판은 6개를 넘을 수 없습니다."),

    // 파일 에러
    FILE_NOT_FOUND(500, "파일을 읽을 수 없거나 존재하지 않습니다"),
    INVALID_URL(500, "유효하지 않은 파일 URL입니다.");

    private final int code;
    private final String message;

}
