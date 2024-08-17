package com.commtalk.domain.board.dto;

import com.commtalk.domain.board.entity.Board;
import com.commtalk.domain.board.entity.PinnedBoard;
import com.commtalk.domain.post.dto.PostPreviewDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Schema(description = "핀고정 게시판 정보")
public class PinnedBoardDTO {

    @Schema(description = "핀고정 게시판 식별자")
    private Long pinnedBoardId;

    @Schema(description = "게시판 식별자")
    private Long boardId;

    @Schema(description = "게시판 이름")
    private String boardName;

    @Schema(description = "게시판 설명")
    private String desc;

    @Setter
    @Schema(description = "게시글 미리보기 목록")
    private List<PostPreviewDTO> posts;

    public static PinnedBoardDTO from(PinnedBoard pinnedBoard) {
        Board board = pinnedBoard.getBoard();

        return PinnedBoardDTO.builder()
                .pinnedBoardId(pinnedBoard.getId())
                .boardId(board.getId())
                .boardName(board.getBoardName())
                .desc(board.getDescription())
                .build();
    }

}
