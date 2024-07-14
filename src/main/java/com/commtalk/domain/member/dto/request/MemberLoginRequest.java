package com.commtalk.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "로그인 정보")
public class MemberLoginRequest {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Schema(description = "닉네임")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Schema(description = "비밀번호")
    private String password;

}
