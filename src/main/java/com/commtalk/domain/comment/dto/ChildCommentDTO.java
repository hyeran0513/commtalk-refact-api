package com.commtalk.domain.comment.dto;

import com.commtalk.domain.member.dto.MemberSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "대댓글 정보")
public class ChildCommentDTO {

    @Schema(description = "댓글 식별자")
    private Long commentId;

    @Schema(description = "상위 댓글 식별자")
    private Long parentId;

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "작성자 정보")
    private MemberSimpleDTO writer;

    @Schema(description = "좋아요 수")
    private long likeCount;

    @Schema(description = "최근 수정 일시")
    private String updatedAt;

}
