package com.commtalk.common.exception;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.post.exception.MemberActivitySetException;
import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 커스템 예외 처리
     * @param e CustomException 객체
     * @return ResponseEntity 객체
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDTO<String>> handleCustomException(CustomException e) {
        log.error(e);
        return ResponseDTO.of(HttpStatus.valueOf(e.getErrorCode().getCode()), e.getErrorCode().getMessage());
    }

    /**
     * 데이터 검증 시 발생한 예외 처리 
     * @param e MethodArgumentNotValidException 객체
     * @return ResponseEntity 객체
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e);
        Map<String, String> errorMap = new HashMap<>();
        e.getAllErrors().forEach(
                c -> errorMap.put(((FieldError) c).getField(), c.getDefaultMessage())
        );
        return ResponseDTO.of(HttpStatus.BAD_REQUEST, errorMap);
    }

    /**
     * 나머지 예외 처리
     * @param e Exception 객체
     * @return ResponseEntity 객체
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<String>> handleException(Exception e) {
        log.error(e);
        return ResponseDTO.of(HttpStatus.valueOf(ErrorCode.SERVER_INTERNAL_ERROR.getCode()),
                ErrorCode.SERVER_INTERNAL_ERROR.getMessage());
    }

    /**
     * 회원 활동 객체 구성 중 발생한 예외 처리
     * @param e MemberActivitySetException 객체
     * @return  ResponseEntity 객체
     */
    @ExceptionHandler(MemberActivitySetException.class)
    public ResponseEntity<ResponseDTO<String>> handleMemberActivitySetException(MemberActivitySetException e) {
        return ResponseDTO.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * 권한 예외 처리 핸들러
     * @param e PermissionException 객체
     * @return ResponseEntity 객체
     */
    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<ResponseDTO<String>> handlePermissionException(PermissionException e) {
        return ResponseDTO.of(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO<String>> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseDTO.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ResponseDTO<String>> handleJwtException(JwtException e) {
        return ResponseDTO.of(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDTO<String>> handleBadCredentialsException() {
        return ResponseDTO.of(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없거나 비밀번호가 틀렸습니다.");
    }
    
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ResponseDTO<String>> handleInsufficientAuthenticationException() {
        return ResponseDTO.of(HttpStatus.UNAUTHORIZED, "관리자 계정만 접근 가능합니다.");
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    ResponseEntity<ResponseDTO<String>> handleAuthenticationServiceException(AuthenticationServiceException e) {
        return ResponseDTO.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

}

