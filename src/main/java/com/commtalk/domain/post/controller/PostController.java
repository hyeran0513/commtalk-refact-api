package com.commtalk.domain.post.controller;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.board.dto.BoardDTO;
import com.commtalk.domain.board.dto.BoardSimpleDTO;
import com.commtalk.domain.board.service.BoardService;
import com.commtalk.domain.post.dto.*;
import com.commtalk.domain.post.service.CommentService;
import com.commtalk.domain.post.service.PostService;
import com.commtalk.security.JwtAuthenticationProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Tag(name = "board", description = "게시판 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/boards")
public class PostController {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final BoardService boardSvc;
    private final PostService postSvc;
    private final CommentService commentSvc;

    @Operation(summary = "게시글 목록 조회")
    @GetMapping(path = "/{boardId}/posts")
    public ResponseEntity<PostPageDTO> getPosts(@PathVariable Long boardId, @PageableDefault Pageable pageable) {
        boardSvc.isExistsBoard(boardId); // 게시판이 존재하는지 확인
        PostPageDTO postPageDto = postSvc.getPostsByBoard(boardId, pageable); // 게시글 목록 조회
        return ResponseEntity.ok(postPageDto);
    }

    @Operation(summary = "인기 게시글 목록 조회")
    @GetMapping(path = "/{boardId}/posts/popular")
    public ResponseEntity<List<PostPreviewDTO>> getPostsByViews() {
        List<PostPreviewDTO> postPreviewDtoList = postSvc.getPostPreviewsByViews(); // 게시글 목록 조회
        return ResponseEntity.ok(postPreviewDtoList);
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping(path = "/{boardId}/posts/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long boardId, @PathVariable Long postId) {
        BoardDTO boardDto = boardSvc.getBoard(boardId); // 게시판 조회
        PostDTO postDto = postSvc.getPost(postId); // 게시글 조회
        postDto.setBoard(boardDto);
        return ResponseEntity.ok(postDto);
    }

    @Operation(summary = "게시글 생성")
    @PostMapping(path = "/{boardId}/posts")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseDTO<String>> createPost(@PathVariable Long boardId, @RequestBody @Valid CreatePostDTO postDto,
                                                  HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        boardSvc.isExistsBoard(boardId); // 게시판이 존재하는지 확인
        postSvc.createPost(memberId, boardId, postDto); // 게시글 생성
        return ResponseDTO.of(HttpStatus.OK, "게시글을 생성했습니다.");
    }

    @Operation(summary = "게시글 댓글 목록 조회")
    @GetMapping(path = "/{boardId}/posts/{postId}/comments")
    public ResponseEntity<List<ParentCommentDTO>> getCommentsByPost(@PathVariable Long boardId, @PathVariable Long postId) {
        postSvc.isExistsPost(postId); // 게시글이 존재하는지 확인
        List<ParentCommentDTO> commentDtoList = commentSvc.getCommentsByPost(postId); // 게시글 댓글 목록 조회
        return ResponseEntity.ok(commentDtoList);
    }


    @Operation(summary = "게시글 댓글 생성")
    @PostMapping(path = "/{boardId}/posts/{postId}/comments")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseDTO<String>> createComment(@PathVariable Long boardId, @PathVariable Long postId,
                                                             @RequestBody @Valid CreateCommentDTO commentDto, HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        postSvc.isExistsPost(postId); // 게시글이 존재하는지 확인
        commentSvc.createComment(memberId, postId, commentDto); // 댓글 생성
        return ResponseDTO.of(HttpStatus.OK, "댓글을 생성했습니다.");
    }

}
