package com.commtalk.domain.post.dto;

import com.commtalk.domain.post.entity.Comment;
import com.commtalk.domain.member.dto.MemberSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Schema(description = "익명 여부")
    private boolean anonymousYN;

    @Schema(description = "종아요 수")
    private long likeCount;

    @Schema(description = "최근 수정 일시")
    private String updatedAt;

    @Schema(description = "대댓글 목록")
    private List<ChildCommentDTO> children;

    @Setter
    @Schema(description = "대댓글 수")
    private long childCount;

    public void addChildComment(ChildCommentDTO childCommentDto) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(childCommentDto);
    }

    public static ParentCommentDTO from(Comment comment) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return ParentCommentDTO.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .writer(MemberSimpleDTO.from(comment.getWriter()))
                .anonymousYN(comment.isAnonymousYN())
                .likeCount(comment.getLikeCount())
                .updatedAt(sdf.format(comment.getUpdatedAt()))
                .build();
    }

}
