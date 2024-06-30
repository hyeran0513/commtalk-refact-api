package com.commtalk.domain.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "핀고정 게시판 순서 변경 정보")
public class PinnedBoardReorderDTO {

    @Schema(description = "핀고정 게시판 순서")
    private int order;

}
