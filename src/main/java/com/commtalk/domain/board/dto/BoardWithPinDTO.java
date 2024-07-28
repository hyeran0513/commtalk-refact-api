package com.commtalk.domain.board.dto;

import com.commtalk.domain.board.entity.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Schema(description = "게시판 핀고정 정보")
public class BoardWithPinDTO {

    @Schema(description = "게시판 식별자")
    private Long boardId;

    @Schema(description = "게시판 이름")
    private String boardName;

    @Setter
    @Schema(description = "핀고정 게시판 식별자")
    private Long pinnedBoardId;

    public static BoardWithPinDTO from(Board board) {
        return BoardWithPinDTO.builder()
                .boardId(board.getId())
                .boardName(board.getName())
                .build();
    }

}
