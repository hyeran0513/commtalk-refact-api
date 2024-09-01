package com.commtalk.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "회원 변경 정보")
public class MemberUpdateRequest {

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
