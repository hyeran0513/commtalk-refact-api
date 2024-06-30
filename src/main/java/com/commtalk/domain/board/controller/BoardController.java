package com.commtalk.domain.board.controller;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.board.dto.BoardDTO;
import com.commtalk.domain.board.dto.PinnedBoardDTO;
import com.commtalk.domain.board.dto.PinnedBoardReorderDTO;
import com.commtalk.domain.board.service.BoardService;
import com.commtalk.security.JwtAuthenticationProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "게시판 조회")
    @GetMapping(path = "/{boardId}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable Long boardId) {
        BoardDTO boardDto = boardSvc.getBoard(boardId);
        return ResponseEntity.ok(boardDto);
    }

    @Operation(summary = "핀고정 게시판 조회")
    @GetMapping(path = "/pinned")
    public ResponseEntity<List<PinnedBoardDTO>> getPinnedBoards(HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        List<PinnedBoardDTO> pinnedBoardDtoList = boardSvc.getPinnedBoards(memberId);
        return ResponseEntity.ok(pinnedBoardDtoList);
    }

    @Operation(summary = "핀고정 게시판 추가")
    @PostMapping(path = "/pinned")
    public ResponseEntity<ResponseDTO<String>> createPinnedBoard(@RequestBody BoardDTO boardDto) {
        return ResponseDTO.of(HttpStatus.OK, "");
    }

    @Operation(summary = "핀고정 게시판 삭제")
    @DeleteMapping(path = "/pinned/{pinnedId}")
    public ResponseEntity<ResponseDTO<String>> deletePinnedBoard(@PathVariable Long pinnedId) {
        return ResponseDTO.of(HttpStatus.OK, "");
    }

    @Operation(summary = "핀고정 게시판 순서 변경")
    @PatchMapping(path = "/pinned/{pinnedId}/reorder")
    public ResponseEntity<ResponseDTO<String>> reorderPinnedBoard(@PathVariable Long pinnedId,
                                                                  @RequestBody PinnedBoardReorderDTO reorderDto) {
        return ResponseDTO.of(HttpStatus.OK, "");
    }

}
