package com.commtalk.domain.board.controller;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.board.dto.BoardDTO;
import com.commtalk.domain.board.dto.PinAndUnpinBoardDTO;
import com.commtalk.domain.board.dto.PinnedBoardDTO;
import com.commtalk.domain.board.service.BoardService;
import com.commtalk.domain.post.dto.PostPreviewDTO;
import com.commtalk.domain.post.service.PostService;
import com.commtalk.security.JwtAuthenticationProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "board", description = "게시판 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/boards")
public class BoardController {

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

}
