package com.commtalk.domain.member.dto;

import com.commtalk.domain.member.entity.Member;
import com.commtalk.domain.member.entity.MemberPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "회원 기본 정보")
public class MemberSimpleDTO {

    @Schema(description = "회원 식별자")
    private Long memberId;

    @Schema(description = "닉네임")
    private String nickname;

    public static MemberSimpleDTO from(Member member) {
        return MemberSimpleDTO.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();
    }

}
