package com.commtalk.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 기타 서버 에러
    SERVER_INTERNAL_ERROR(500, "서버 내부에서 에러가 발생했습니다."),

    // 회원 에러
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    DUPLICATE_NICKNAME(409, "닉네임이 중복됩니다."),
    MISMATCH_CURRENT_PASSWORD(400, "현재 비밀번호가 일치하지 않습니다."),
    MISMATCH_CONFIRM_PASSWORD(400, "비밀번호 확인 값이 일치하지 않습니다."),

    // 게시판 에러
    EXCEEDED_PIN_LIMIT(400, "핀 고정 게시판은 6개를 넘을 수 없습니다.");

    private final int code;
    private final String message;

}
