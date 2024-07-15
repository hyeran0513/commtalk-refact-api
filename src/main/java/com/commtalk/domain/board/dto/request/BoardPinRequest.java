package com.commtalk.domain.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "게시판 핀고정 및 해제 정보")
public class BoardPinRequest {

    @Schema(description = "핀고정 게시판 식별자")
    private Long pinnedBoardId;

    @Schema(description = "게시판 식별자")
    private Long boardId;

}
