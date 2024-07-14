package com.commtalk.domain.post.dto;

import com.commtalk.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Schema(description = "게시글 미리보기 정보")
public class PostPreviewDTO {

    @Schema(description = "게시글 식별자")
    private Long postId;

    @Schema(description = "게시판명")
    private String boardName;

    @Schema(description = "게시글 제목")
    private String title;

    @Schema(description = "댓글 허용 여부")
    private boolean commentableYN;

    @Setter
    @Schema(description = "댓글 수")
    private long commentCnt;

    @Schema(description = "조회 수")
    private long viewCnt;

    @Schema(description = "좋아요 수")
    private long likeCnt;

    public static PostPreviewDTO of(Post post) {
        return PostPreviewDTO.builder()
                .postId(post.getId())
                .boardName(post.getBoard().getName())
                .title(post.getTitle())
                .commentableYN(post.isCommentableYN())
                .viewCnt(post.getViewCount())
                .likeCnt(post.getLikeCount())
                .build();
    }

}
