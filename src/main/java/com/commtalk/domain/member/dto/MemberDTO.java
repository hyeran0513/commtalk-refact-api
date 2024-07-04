package com.commtalk.domain.member.dto;

import com.commtalk.domain.member.entity.MemberPassword;
import com.commtalk.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "회원 정보")
public class MemberDTO {

    @Schema(description = "회원 식별자")
    private Long memberId;

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "회원명")
    private String username;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "전화번호")
    private String phone;

    public static MemberDTO from(Member member) {
        return MemberDTO.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .username(member.getMemberName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .build();
    }

}
