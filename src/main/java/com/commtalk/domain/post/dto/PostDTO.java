package com.commtalk.domain.post.dto;

import com.commtalk.domain.board.dto.BoardDTO;
import com.commtalk.domain.member.dto.MemberSimpleDTO;
import com.commtalk.domain.post.entity.Post;
import com.commtalk.domain.post.entity.PostHashtag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.List;

@Getter
@Builder
@Schema(description = "게시글 정보")
public class PostDTO {

    @Schema(description = "게시글 식별자")
    private Long postId;

    @Schema(description = "게시글 제목")
    private String title;

    @Schema(description = "게시글 내용")
    private String content;

    @Schema(description = "작성자 정보")
    private MemberSimpleDTO author;

    @Setter
    @Schema(description = "게시판 정보")
    private BoardDTO board;

    @Schema(description = "최근 수정 일시")
    private String updatedAt;

    @Schema(description = "댓글 허용 여부")
    private boolean commentableYN;

    @Schema(description = "조회 수")
    private long viewCnt;

    @Schema(description = "좋아요 수")
    private long likeCnt;

    @Schema(description = "해시태그 목록")
    private List<PostHashtagDTO> hashtags;

    public static PostDTO from(Post post, List<PostHashtag> postHashtagList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return PostDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(MemberSimpleDTO.from(post.getAuthor()))
                .updatedAt(sdf.format(post.getUpdatedAt()))
                .commentableYN(post.isCommentableYN())
                .viewCnt(post.getViewCount())
                .likeCnt(post.getLikeCount())
                .hashtags(postHashtagList.stream().map(PostHashtagDTO::from).toList())
                .build();
    }

}
