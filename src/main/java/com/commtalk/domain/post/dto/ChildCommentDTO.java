package com.commtalk.domain.post.dto;

import com.commtalk.domain.post.entity.Comment;
import com.commtalk.domain.member.dto.MemberSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.text.SimpleDateFormat;

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

    @Schema(description = "익명 여부")
    private boolean anonymousYN;

    @Schema(description = "좋아요 수")
    private long likeCount;

    @Schema(description = "최근 수정 일시")
    private String updatedAt;

    @Schema(description = "좋아요 여부")
    private boolean likeYN;

    public static ChildCommentDTO from(Comment comment, boolean likeYN) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return ChildCommentDTO.builder()
                .commentId(comment.getId())
                .parentId(comment.getParent().getId())
                .content(comment.getContent())
                .writer(MemberSimpleDTO.from(comment.getWriter()))
                .anonymousYN(comment.isAnonymousYN())
                .likeCount(comment.getLikeCount())
                .updatedAt(sdf.format(comment.getUpdatedAt()))
                .likeYN(likeYN)
                .build();
    }

}
