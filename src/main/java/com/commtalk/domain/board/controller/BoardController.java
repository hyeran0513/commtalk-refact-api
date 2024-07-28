package com.commtalk.domain.board.controller;

import com.commtalk.domain.board.dto.BoardDTO;
import com.commtalk.domain.board.dto.BoardWithPinDTO;
import com.commtalk.domain.board.service.BoardService;
import com.commtalk.security.JwtAuthenticationProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "board", description = "게시판 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/boards")
public class BoardController {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final BoardService boardSvc;

    @Operation(summary = "전체 게시판 조회")
    @GetMapping(path = "")
    public ResponseEntity<List<BoardDTO>> getAllBoard() {
        List<BoardDTO> boardDtoList = boardSvc.getAllBoard();
        return ResponseEntity.ok(boardDtoList);
    }

    @Operation(summary = "전체 게시판 조회 (핀고정 여부 포함)")
    @GetMapping(path = "/with-pin")
    public ResponseEntity<List<BoardWithPinDTO>> getAllBoardWithPin(HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        List<BoardWithPinDTO> boardWithPinDtoList = boardSvc.getAllBoardWithPin(memberId);
        return ResponseEntity.ok(boardWithPinDtoList);
    }

    @Operation(summary = "게시판 조회")
    @GetMapping(path = "/{boardId}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Long boardId) {
        BoardDTO boardDto = boardSvc.getBoard(boardId);
        return ResponseEntity.ok(boardDto);
    }

}
