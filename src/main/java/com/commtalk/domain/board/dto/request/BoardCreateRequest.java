package com.commtalk.domain.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "게시판 생성 정보")
public class BoardCreateRequest {

    @NotBlank(message = "게시판 이름은 필수 입력 값입니다.")
    @Schema(description = "게시판 이름")
    private String boardName;

    @Schema(description = "게시판 설명")
    private String desc;

    public static BoardCreateRequest from(String boardName, String desc) {
        return BoardCreateRequest.builder()
                .boardName(boardName)
                .desc(desc)
                .build();
    }

}
