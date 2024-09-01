package com.commtalk.domain.post.controller;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.post.dto.ParentCommentDTO;
import com.commtalk.domain.post.dto.request.CommentCreateRequest;
import com.commtalk.domain.post.dto.request.CommentUpdateRequest;
import com.commtalk.domain.post.service.CommentService;
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

@Tag(name = "comment", description = "게시글 댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/posts/{postId}/comments")
public class CommentController {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final PostService postSvc;
    private final CommentService commentSvc;

    @Operation(summary = "게시글 댓글 목록 조회")
    @GetMapping(path = "")
    public ResponseEntity<List<ParentCommentDTO>> getCommentsByPost(@PathVariable Long postId, HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        postSvc.isExistsPost(postId); // 게시글이 존재하는지 확인
        List<ParentCommentDTO> commentDtoList;
        if (memberId == null) {
            commentDtoList = commentSvc.getCommentsByPost(postId); // 게시글 댓글 목록 조회
        } else {
            commentDtoList = commentSvc.getCommentsByPost(postId, memberId); // 게시글 댓글 목록 조회 (좋아요 여부 포함)
        }
        return ResponseEntity.ok(commentDtoList);
    }


    @Operation(summary = "게시글 댓글 생성")
    @PostMapping(path = "")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseDTO<String>> createComment(@PathVariable Long postId, @RequestBody @Valid CommentCreateRequest createReq,
                                                             HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        postSvc.isExistsPost(postId); // 게시글이 존재하는지 확인
        commentSvc.createComment(memberId, postId, createReq); // 댓글 생성
        return ResponseDTO.of(HttpStatus.OK, "댓글을 생성했습니다.");
    }

    @Operation(summary = "게시글 댓글 수정")
    @PatchMapping(path = "/{commentId}")
    public ResponseEntity<ResponseDTO<String>> updateComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                             @RequestBody @Valid CommentUpdateRequest updateReq, HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        postSvc.isExistsPost(postId); // 게시글이 존재하는지 확인
        commentSvc.updateComment(memberId, commentId, updateReq); // 댓글 수정
        return ResponseDTO.of(HttpStatus.OK, "댓글을 수정했습니다.");
    }

    @Operation(summary = "게시글 댓글 삭제")
    @DeleteMapping(path = "/{commentId}")
    public ResponseEntity<ResponseDTO<String>> deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                             HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        postSvc.isExistsPost(postId); // 게시글이 존재하는지 확인
        commentSvc.deleteComment(memberId, commentId); // 댓글 삭제
        return ResponseDTO.of(HttpStatus.OK, "댓글을 삭제했습니다.");
    }

    @Operation(summary = "게시글 댓글 좋아요 및 취소")
    @PostMapping(path = "/{commentId}/like")
    public ResponseEntity<ParentCommentDTO> likeComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                       HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        postSvc.isExistsPost(postId); // 게시글이 존재하는지 확인

        ParentCommentDTO commentDto;
        if (!commentSvc.isLikeComment(memberId, commentId)) {
            commentDto = commentSvc.likeComment(memberId, commentId); // 좋아요
        } else {
            commentDto = commentSvc.unlikeComment(memberId, commentId); // 좋아요 취소
        }
        return ResponseEntity.ok(commentDto);
    }

}
