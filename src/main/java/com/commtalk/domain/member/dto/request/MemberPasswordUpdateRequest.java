package com.commtalk.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "회원 비밀번호 변경 정보")
public class MemberPasswordUpdateRequest {

    @NotBlank(message = "현재 비밀번호는 필수 입력 값입니다.")
    @Schema(description = "현재 비밀번호")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호는 필수 입력 값입니다.")
    @Schema(description = "새 비밀번호")
    private String newPassword;

    @NotBlank(message = "새 비밀번호 확인은 필수 입력 값입니다.")
    @Schema(description = "새 비밀번호 확인")
    private String confirmPassword;
    
}
