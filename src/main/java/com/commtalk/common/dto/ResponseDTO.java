package com.commtalk.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@Schema(description = "기본 응답")
public class ResponseDTO<T> {

    @Schema(description = "상태 코드")
    private String code;

    @Schema(description = "메시지")
    private T message;

    public static <T> ResponseEntity<ResponseDTO<T>> of(HttpStatus status, T message) {
        return ResponseEntity
                .status(status)
                .body(ResponseDTO.<T>builder()
                        .code(status.name())
                        .message(message)
                        .build());
    }

}