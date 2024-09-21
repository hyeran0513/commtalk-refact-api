package com.commtalk.domain.board.dto;

import com.commtalk.domain.board.entity.BoardRequest;
import com.commtalk.domain.member.dto.MemberSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "게시판 요청 정보")
public class BoardRequestDTO {

    @Schema(description = "게시판 요청 식별자")
    private Long boardReqId;

    @Schema(description = "게시판 식별자")
    private Long boardId;

    @Schema(description = "요청자 정보")
    private MemberSimpleDTO requester;

    @Schema(description = "승인자 정보")
    private MemberSimpleDTO approver;

    @Schema(description = "게시판 이름")
    private String boardName;

    @Schema(description = "게시판 설명")
    private String description;

    @Schema(description = "요청 상태")
    private String reqSts;
    
    @Schema(description = "취소 여부")
    private boolean canceledYN;

    public static BoardRequestDTO of(BoardRequest boardReq) {
        int reqSts = boardReq.getReqSts();
        return BoardRequestDTO.builder()
                .boardReqId(boardReq.getId())
                .boardId(boardReq.getBoardId())
                .requester(MemberSimpleDTO.from(boardReq.getRequester()))
                .approver((boardReq.getApprover() != null) ? MemberSimpleDTO.from(boardReq.getApprover()) : null)
                .boardName(boardReq.getBoardName())
                .description(boardReq.getDescription())
                .reqSts((reqSts == 0) ? "대기" : (reqSts == 1) ? "승인" : "거절")
                .canceledYN(boardReq.isCanceledYN())
                .build();
    }

}
