package com.commtalk.domain.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "핀고정 게시판 추가 및 삭제 정보")
public class PinAndUnpinBoardDTO {

    @Schema(description = "추가할 게시판 식별자 목록")
    private List<Long> pinBoardIds;

    @Schema(description = "삭제할 게시판 식별자 목록")
    private List<Long> unpinBoardIds;

}
