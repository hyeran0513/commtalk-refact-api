package com.commtalk.common.exception;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.auth.exception.DuplicateNicknameException;
import com.commtalk.domain.auth.exception.MemberIdNullException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public ResponseEntity<ResponseDTO> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseDTO.of(HttpStatus.CONFLICT, e.getMessage());
    }

    /**
     * 닉네임 중복 여부에 대한 예외 처리
     * @param e DuplicateNicknameException 객체
     * @return  ResponseEntity 객체
     */
    @ExceptionHandler(DuplicateNicknameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseDTO> handleDuplicateNicknameException(DuplicateNicknameException e) {
        return ResponseDTO.of(HttpStatus.CONFLICT, e.getMessage());
    }

    /**
     * 회원 아이디 생성 실패에 대한 예외 처리
     * @param e MemberIdNullException 객체
     * @return  ResponseEntity 객체
     */
    @ExceptionHandler(MemberIdNullException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseDTO> handleMemberIdNullException(MemberIdNullException e) {
        return ResponseDTO.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseDTO> handleSignatureException() {
        return ResponseDTO.of(HttpStatus.INTERNAL_SERVER_ERROR, "토큰이 유효하지 않습니다.");
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseDTO> handleMalformedJwtException() {
        return ResponseDTO.of(HttpStatus.INTERNAL_SERVER_ERROR, "올바르지 않은 토큰입니다.");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseDTO> handleExpiredJwtException() {
        return ResponseDTO.of(HttpStatus.INTERNAL_SERVER_ERROR, "토큰이 만료되었습니다. 다시 로그인해주세요.");
    }


}

