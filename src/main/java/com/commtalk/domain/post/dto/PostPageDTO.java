package com.commtalk.domain.post.dto;

import com.commtalk.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@Schema(description = "게시글 페이지 정보")
public class PostPageDTO {

    @Schema(description = "전체 페이지 수")
    private int totalPages;

    @Schema(description = "현재 페이지 번호")
    private int pageNumber;

    @Schema(description = "이전 페이지 번호")
    private int previous;

    @Schema(description = "다음 페이지 번호")
    private int next;

    @Schema(description = "게시글 목록")
    private List<PostSimpleDTO> posts;

    public static PostPageDTO of(Page<Post> postPage) {
        return PostPageDTO.builder()
                .totalPages(postPage.getTotalPages())
                .pageNumber(postPage.getNumber())
                .previous((postPage.hasPrevious()) ? postPage.previousPageable().getPageNumber() : -1)
                .next((postPage.hasNext()) ? postPage.nextPageable().getPageNumber() : -1)
                .posts(postPage.getContent().stream().map(PostSimpleDTO::of).toList())
                .build();
    }

}
