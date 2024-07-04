package com.commtalk.domain.comment.dto;

import com.commtalk.domain.member.dto.MemberSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "댓글 정보")
public class ParentCommentDTO {

    @Schema(description = "댓글 식별자")
    private Long commentId;

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "작성자 정보")
    private MemberSimpleDTO writer;

    @Schema(description = "종아요 수")
    private long likeCount;

    @Schema(description = "최근 수정 일시")
    private String updatedAt;

    @Schema(description = "대댓글 목록")
    private List<ChildCommentDTO> children;

    @Schema(description = "대댓글 수")
    private long childCount;

}
