package com.commtalk.domain.board.dto;

import com.commtalk.domain.board.entity.BoardRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@Schema(description = "게시판 요청 페이지 정보")
public class BoardRequestPageDTO {

    @Schema(description = "전체 페이지 수")
    private int totalPages;

    @Schema(description = "현재 페이지 번호")
    private int pageNumber;

    @Schema(description = "이전 페이지 번호")
    private int previous;

    @Schema(description = "다음 페이지 번호")
    private int next;

    @Schema(description = "게시판 요청 목록")
    private List<BoardRequestDTO> boardReqs;

    public static BoardRequestPageDTO of(Page<BoardRequest> boardReqPage) {
        return BoardRequestPageDTO.builder()
                .totalPages(boardReqPage.getTotalPages())
                .pageNumber(boardReqPage.getNumber())
                .previous((boardReqPage.hasPrevious()) ? boardReqPage.previousPageable().getPageNumber() : -1)
                .next((boardReqPage.hasNext()) ? boardReqPage.nextPageable().getPageNumber() : -1)
                .boardReqs(boardReqPage.getContent().stream().map(BoardRequestDTO::of).toList())
                .build();
    }

}
