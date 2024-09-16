package com.commtalk.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "회원 좋아요 정보")
public class MemberLikeDTO {

    @Schema(description = "게시글(댓글) 좋아요 수")
    private long likeCnt;

    @Schema(description = "회원 좋아요 여부")
    private boolean likeYN;

    public static MemberLikeDTO from(long likeCnt, boolean likeYN) {
        return MemberLikeDTO.builder()
                .likeCnt(likeCnt)
                .likeYN(likeYN)
                .build();
    }
    
}
