package com.commtalk.domain.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "핀고정 게시판 정보")
public class PinnedBoardDTO {

    @Schema(description = "게시판 식별자")
    private Long boardId;

    @Schema(description = "게시판 이름")
    private String boardName;

    @Schema(description = "게시판 설명")
    private String desc;

    @Schema(description = "게시글 미리보기 목록")
    private List<PostPreviewDTO> posts;

}
