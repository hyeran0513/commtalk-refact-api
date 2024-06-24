package com.commtalk.common.exception;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.auth.exception.DuplicateNicknameException;
import com.commtalk.domain.auth.exception.MemberIdNullException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 사용자 존재 여부에 대한 예외 처리
     * @param e UsernameNotFoundException 객체
     * @return  ResponseEntity 객체
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseDTO.of(HttpStatus.CONFLICT, e.getMessage());
    }

    /**
     * 닉네임 중복 여부에 대한 예외 처리
     * @param e DuplicateNicknameException 객체
     * @return  ResponseEntity 객체
     */
    @ExceptionHandler(DuplicateNicknameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleDuplicateNicknameException(DuplicateNicknameException e) {
        return ResponseDTO.of(HttpStatus.CONFLICT, e.getMessage());
    }

    /**
     * 회원 아이디 생성 실패에 대한 예외 처리
     * @param e MemberIdNullException 객체
     * @return  ResponseEntity 객체
     */
    @ExceptionHandler(MemberIdNullException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleMemberIdNullException(MemberIdNullException e) {
        return ResponseDTO.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

}

