package com.commtalk.domain.board.dto;

import com.commtalk.domain.board.entity.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "게시판 기본 정보")
public class BoardSimpleDTO {

    @Schema(description = "게시판 식별자")
    private Long boardId;

    @Schema(description = "게시판 이름")
    private String boardName;

    public static BoardSimpleDTO from(Board board) {
        return BoardSimpleDTO.builder()
                .boardId(board.getId())
                .boardName(board.getName())
                .build();
    }

}
