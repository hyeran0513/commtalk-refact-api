package com.commtalk.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "회원가입 정보")
public class MemberJoinRequest {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Schema(description = "닉네임")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Schema(description = "비밀번호")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
    @Schema(description = "비밀번호 확인")
    private String confirmPassword;

    @NotBlank(message = "회원명은 필수 입력 값입니다.")
    @Schema(description = "회원명")
    private String username;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Schema(description = "이메일")
    private String email;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Schema(description = "전화번호")
    private String phone;

}
