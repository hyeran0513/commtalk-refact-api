package com.commtalk.domain.member.dto;

import com.commtalk.domain.member.entity.Account;
import com.commtalk.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "회원 정보")
public class MemberDTO {

    @Schema(description = "회원 식별자")
    private Long memberId;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Schema(description = "닉네임")
    private String nickname;

    @NotBlank(message = "회원명은 필수 입력 값입니다.")
    @Schema(description = "회원명")
    private String username;

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Schema(description = "이메일")
    private String email;

    @Schema(description = "전화번호")
    private String phone;

    public static MemberDTO from(Member member, Account account) {
        return MemberDTO.builder()
                .memberId(member.getId())
                .nickname(account.getNickname())
                .username(member.getMemberName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .build();
    }

}
