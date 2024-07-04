package com.commtalk.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "해시태그 정보")
public class PostHashtagDTO {

    @Schema(description = "해시태그 식별자")
    private Long hashtagId;

    @Schema(description = "해시태그")
    private String hashtag;
    
}
