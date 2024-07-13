package com.commtalk.domain.post.dto;

import com.commtalk.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;

@Getter
@Builder
@Schema(description = "게시글 기본 정보")
public class PostSimpleDTO {

    @Schema(description = "게시글 식별자")
    private Long postId;

    @Schema(description = "게시글 제목")
    private String title;

    @Schema(description = "게시글 내용 미리보기")
    private String previewContent;

    @Schema(description = "작성자")
    private String authorName;

    @Schema(description = "최근 수정 일시")
    private String updatedAt;

    @Schema(description = "댓글 허용 여부")
    private boolean commentableYN;

    @Setter
    @Schema(description = "댓글 수")
    private long commentCnt;

    @Schema(description = "조회 수")
    private long viewCnt;

    @Schema(description = "좋아요 수")
    private long likeCnt;

    public static PostSimpleDTO of(Post post) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String content = post.getContent();

        return PostSimpleDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .previewContent((content.length() > 10) ? content.substring(0, 10) + " ..." : content)
                .authorName((post.isAnonymousYN()) ? "익명" : post.getAuthor().getMemberName())
                .updatedAt(sdf.format(post.getUpdatedAt()))
                .commentableYN(post.isCommentableYN())
                .viewCnt(post.getViewCount())
                .likeCnt(post.getLikeCount())
                .build();
    }

}
