package com.commtalk.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "회원 비밀번호 확인 정보")
public class MemberPasswordConfirmRequest {

    @NotBlank(message = "현재 비밀번호는 필수 입력 값입니다.")
    @Schema(description = "현재 비밀번호")
    private String currentPassword;

    // 필드가 하나일 때 기본 생성자가 생성되지 않는 에러 발생
    @JsonCreator
    public MemberPasswordConfirmRequest(@JsonProperty("currentPassword") String password) {
        this.currentPassword = password;
    }

}
