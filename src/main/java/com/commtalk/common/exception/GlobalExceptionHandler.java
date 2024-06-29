package com.commtalk.common.exception;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.member.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 사용자 존재 여부에 대한 예외 처리
     * @param e UsernameNotFoundException 객체
     * @return  ResponseEntity 객체
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDTO<String>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseDTO.of(HttpStatus.CONFLICT, e.getMessage());
    }

    /**
     * 닉네임 중복 여부에 대한 예외 처리
     * @param e DuplicateNicknameException 객체
     * @return  ResponseEntity 객체
     */
    @ExceptionHandler(DuplicateNicknameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseDTO<String>> handleDuplicateNicknameException(DuplicateNicknameException e) {
        return ResponseDTO.of(HttpStatus.CONFLICT, e.getMessage());
    }

    /**
     * 회원 아이디가 null인 경우에 대한 예외 처리
     * @param e MemberIdNullException 객체
     * @return  ResponseEntity 객체
     */
    @ExceptionHandler(MemberIdNullException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseDTO<String>> handleMemberIdNullException(MemberIdNullException e) {
        return ResponseDTO.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDTO<String>> handleMemberNotFoundException(MemberNotFoundException e) {
        return ResponseDTO.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AccountRoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDTO<String>> handleAccountRoleNotFoundException(AccountRoleNotFoundException e) {
        return ResponseDTO.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDTO<String>> handleAccountNotFoundException(AccountRoleNotFoundException e) {
        return ResponseDTO.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseDTO<String>> handleJwtException(JwtException e) {
        return ResponseDTO.of(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseDTO<String>> handleBadCredentialsException() {
        return ResponseDTO.of(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없거나 비밀번호가 틀렸습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getAllErrors().forEach(
                c -> errorMap.put(((FieldError) c).getField(), c.getDefaultMessage())
        );
        return ResponseDTO.of(HttpStatus.BAD_REQUEST, errorMap);
    }

}

