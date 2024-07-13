package com.commtalk.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "게시글 생성 정보")
public class PostCreateRequest {

    @NotBlank(message = "게시글 제목은 필수 입력 값입니다.")
    @Schema(description = "게시글 제목")
    private String title;

    @NotNull(message = "게시글 내용은 널일 수 없습니다.")
    @Schema(description = "게시글 내용")
    private String content;

    @Schema(description = "익명 여부")
    private boolean anonymousYN;

    @Schema(description = "댓글 허용 여부")
    private boolean commentableYN;

    @Schema(description = "해시태그 목록")
    private List<String> hashtags;

}
