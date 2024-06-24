package com.commtalk.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "회원가입 DTO")
public class JoinDTO {

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "비밀번호")
    private String password;

    @Schema(description = "회원명")
    private String username;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "전화번호")
    private String phone;

}
