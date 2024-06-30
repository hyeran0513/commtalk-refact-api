package com.commtalk.domain.board.dto;

import com.commtalk.domain.board.entity.PinnedBoard;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "핀고정 게시판 정보")
public class PinnedBoardDTO {

    @Schema(description = "핀고정 게시판 식별자")
    private Long pinnedId;

    @Schema(description = "핀고정 게시판 순서")
    private int order;

    @Schema(description = "게시판 정보")
    private BoardDTO board;

    public static PinnedBoardDTO from(PinnedBoard pinnedBoard) {
        return PinnedBoardDTO.builder()
                .pinnedId(pinnedBoard.getId())
                .board(BoardDTO.from(pinnedBoard.getBoard()))
                .order(pinnedBoard.getPinnedOrder())
                .build();
    }
    
}
