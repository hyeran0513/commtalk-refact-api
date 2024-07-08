package com.commtalk.domain.post.dto;

import com.commtalk.domain.post.entity.PostHashtag;
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

    public static PostHashtagDTO from(PostHashtag hashtag) {
        return PostHashtagDTO.builder()
                .hashtagId(hashtag.getId())
                .hashtag(hashtag.getHashtag())
                .build();
    }
    
}
