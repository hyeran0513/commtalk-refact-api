package com.commtalk.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "회원 스크랩 정보")
public class MemberScrapDTO {

    @Schema(description = "게시글 스크랩 수")
    private long scrapCnt;

    @Schema(description = "회원 스크랩 여부")
    private boolean scrapYN;

    public static MemberScrapDTO from(long scrapCnt, boolean scrapYN) {
        return MemberScrapDTO.builder()
                .scrapCnt(scrapCnt)
                .scrapYN(scrapYN)
                .build();
    }
    
}
