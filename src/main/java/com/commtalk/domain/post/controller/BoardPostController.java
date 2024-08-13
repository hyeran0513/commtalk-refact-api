package com.commtalk.domain.post.controller;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.board.dto.BoardDTO;
import com.commtalk.domain.board.service.BoardService;
import com.commtalk.domain.post.dto.*;
import com.commtalk.domain.post.dto.request.PostCreateRequest;
import com.commtalk.domain.post.entity.ActivityType;
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

@Tag(name = "post", description = "게시글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/boards/{boardId}/posts")
public class BoardPostController {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final BoardService boardSvc;
    private final PostService postSvc;
    private final CommentService commentSvc;

    @Operation(summary = "게시판 게시글 목록 조회")
    @GetMapping(path = "")
    public ResponseEntity<PostPageDTO> getPosts(@PathVariable Long boardId, @RequestParam(required = false) String keyword,
                                                @PageableDefault Pageable pageable) {
        boardSvc.isExistsBoard(boardId); // 게시판이 존재하는지 확인
        PostPageDTO postPageDto = (keyword == null) ? postSvc.getPostsByBoard(boardId, pageable)
                : postSvc.getPostsByBoardAndKeyword(boardId, keyword, pageable); // 게시글 목록 조회
        postPageDto.getPosts()
                .forEach(p -> p.setCommentCnt(commentSvc.getCommentCountByPost(p.getPostId()))); // 게시글 댓글 수 조회
        return ResponseEntity.ok(postPageDto);
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping(path = "/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long boardId, @PathVariable Long postId, HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        BoardDTO boardDto = boardSvc.getBoard(boardId); // 게시판 조회
        PostDTO postDto;
        if (memberId == null) {
            postDto = postSvc.getPost(postId); // 게시글 조회
        } else {
            postDto = postSvc.getPost(postId, memberId); // 게시글 조회 (좋아요, 스크랩 여부 포함)
        }
        postDto.setBoard(boardDto);
        postDto.setCommentCnt(commentSvc.getCommentCountByPost(postDto.getPostId())); // 게시글 댓글 수 조회
        return ResponseEntity.ok(postDto);
    }

    @Operation(summary = "게시글 생성")
    @PostMapping(path = "")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseDTO<String>> createPost(@PathVariable Long boardId, @RequestBody @Valid PostCreateRequest createReq,
                                                  HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        boardSvc.isExistsBoard(boardId); // 게시판이 존재하는지 확인
        Long postId = postSvc.createPost(memberId, boardId, createReq); // 게시글 생성
        return ResponseDTO.of(HttpStatus.OK, String.valueOf(postId));
    }

    @Operation(summary = "게시글 좋아요 및 취소")
    @PostMapping(path = "/{postId}/like")
    public ResponseEntity<ResponseDTO<String>> likePost(@PathVariable Long boardId, @PathVariable Long postId,
                                                        HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        boardSvc.isExistsBoard(boardId); // 게시판이 존재하는지 확인
        String message;
        if (!postSvc.isLikeOrScrapPost(memberId, postId, ActivityType.TypeName.POST_LIKE)) {
            postSvc.likeOrScrapPost(memberId, postId, ActivityType.TypeName.POST_LIKE); // 좋아요
            message = "게시글에 좋아요를 눌렀습니다.";
        } else {
            postSvc.unlikeOrScrapPost(memberId, postId, ActivityType.TypeName.POST_LIKE); // 좋아요 취소
            message = "게시글 좋아요를 취소했습니다.";
        }
        return ResponseDTO.of(HttpStatus.OK, message);
    }

    @Operation(summary = "게시글 스크랩 및 취소")
    @PostMapping(path = "/{postId}/scrap")
    public ResponseEntity<ResponseDTO<String>> scrapPost(@PathVariable Long boardId, @PathVariable Long postId,
                                                        HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        boardSvc.isExistsBoard(boardId); // 게시판이 존재하는지 확인
        String message;
        if (!postSvc.isLikeOrScrapPost(memberId, postId, ActivityType.TypeName.POST_SCRAP)) {
            postSvc.likeOrScrapPost(memberId, postId, ActivityType.TypeName.POST_SCRAP); // 좋아요
            message = "게시글에 좋아요를 눌렀습니다.";
        } else {
            postSvc.unlikeOrScrapPost(memberId, postId, ActivityType.TypeName.POST_SCRAP); // 좋아요 취소
            message = "게시글 좋아요를 취소했습니다.";
        }
        return ResponseDTO.of(HttpStatus.OK, message);
    }

}
