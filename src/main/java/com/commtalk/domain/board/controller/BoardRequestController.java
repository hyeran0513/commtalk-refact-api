package com.commtalk.domain.board.controller;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.board.dto.BoardDTO;
import com.commtalk.domain.board.dto.BoardRequestPageDTO;
import com.commtalk.domain.board.dto.BoardWithPinDTO;
import com.commtalk.domain.board.dto.request.BoardCreateRequest;
import com.commtalk.domain.board.service.BoardService;
import com.commtalk.domain.member.entity.MemberRole;
import com.commtalk.security.JwtAuthenticationProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "board", description = "게시판 요청 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/boards/requests")
public class BoardRequestController {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final BoardService boardSvc;

    @Operation(summary = "게시판 생성 요청 목록 조회")
    @GetMapping(path = "")
    public ResponseEntity<BoardRequestPageDTO> getBoardRequests(@PageableDefault Pageable pageable, HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        String memberRole = jwtAuthenticationProvider.getMemberRole(request);

        BoardRequestPageDTO boardReqPageDto;
        if (MemberRole.RoleName.ROLE_ADMIN.name().equals(memberRole)) {
            boardReqPageDto = boardSvc.getAllBoardRequest(pageable); // 전제 리스트 조회
        } else {
            boardReqPageDto = boardSvc.getBoardRequestsByMember(memberId, pageable); // 해당 회원이 요청한 리스트 조회
        }
        return ResponseEntity.ok(boardReqPageDto);
    }

    @Operation(summary = "게시판 생성 요청")
    @PostMapping(path = "")
    public ResponseEntity<ResponseDTO<String>> createBoardRequest(@RequestBody @Valid BoardCreateRequest createReq,
                                                           HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        boardSvc.createBoardRequest(createReq, memberId);
        return ResponseDTO.of(HttpStatus.OK, "게시판 생성을 요청했습니다.");
    }

    @Operation(summary = "게시판 생성 요청 취소")
    @PostMapping(path = "/{boardReqId}/cancel")
    public ResponseEntity<ResponseDTO<String>> cancelBoardRequest(@PathVariable Long boardReqId, HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        boardSvc.cancelBoardRequest(boardReqId, memberId); // 게시판 요청 취소
        return ResponseDTO.of(HttpStatus.OK, "게시판 생성 요청을 취소했습니다.");
    }

    @Operation(summary = "게시판 생성 요청 승인")
    @PatchMapping(path = "/{boardReqId}/approve")
    public ResponseEntity<ResponseDTO<String>> approveBoardRequest(@PathVariable Long boardReqId, HttpServletRequest request) {
        Long adminId = jwtAuthenticationProvider.getMemberId(request);
        boardSvc.updateBoardRequestStatus(boardReqId, adminId, 1); // 게시판 요청 승인
        return ResponseDTO.of(HttpStatus.OK, "게시판 생성 요청을 승인했습니다. 새 게시판을 생성했습니다.");
    }

    @Operation(summary = "게시판 생성 요청 거절")
    @PatchMapping(path = "/{boardReqId}/reject")
    public ResponseEntity<ResponseDTO<String>> rejectBoardRequest(@PathVariable Long boardReqId, HttpServletRequest request) {
        Long adminId = jwtAuthenticationProvider.getMemberId(request);
        boardSvc.updateBoardRequestStatus(boardReqId, adminId, 2); // 게시판 요청 거절
        return ResponseDTO.of(HttpStatus.OK, "게시판 생성 요청을 거절했습니다.");
    }

}
