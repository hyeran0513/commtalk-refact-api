package com.commtalk.domain.board.dto;

import com.commtalk.domain.board.entity.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "게시판 정보")
public class BoardDTO {

    @Schema(description = "게시판 식별자")
    private Long boardId;

    @Schema(description = "게시판 이름")
    private String boardName;

    @Schema(description = "게시판 설명")
    private String desc;

    public static BoardDTO from(Board board) {
        return BoardDTO.builder()
                .boardId(board.getId())
                .boardName(board.getName())
                .desc(board.getDescription())
                .build();
    }

}
