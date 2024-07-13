package com.commtalk.domain.post.controller;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.post.dto.CreateCommentDTO;
import com.commtalk.domain.post.dto.ParentCommentDTO;
import com.commtalk.domain.post.dto.PostPreviewDTO;
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

@Tag(name = "post", description = "게시글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/posts")
public class PostController {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final PostService postSvc;
    private final CommentService commentSvc;

    @Operation(summary = "인기 게시글 목록 조회")
    @GetMapping(path = "/popular")
    public ResponseEntity<List<PostPreviewDTO>> getPostsByViews() {
        List<PostPreviewDTO> postPreviewDtoList = postSvc.getPostPreviewsByViews(); // 게시글 목록 조회
        return ResponseEntity.ok(postPreviewDtoList);
    }

    @Operation(summary = "게시글 댓글 목록 조회")
    @GetMapping(path = "/{postId}/comments")
    public ResponseEntity<List<ParentCommentDTO>> getCommentsByPost(@PathVariable Long boardId, @PathVariable Long postId) {
        postSvc.isExistsPost(postId); // 게시글이 존재하는지 확인
        List<ParentCommentDTO> commentDtoList = commentSvc.getCommentsByPost(postId); // 게시글 댓글 목록 조회
        return ResponseEntity.ok(commentDtoList);
    }


    @Operation(summary = "게시글 댓글 생성")
    @PostMapping(path = "/{postId}/comments")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseDTO<String>> createComment(@PathVariable Long boardId, @PathVariable Long postId,
                                                             @RequestBody @Valid CreateCommentDTO commentDto, HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        postSvc.isExistsPost(postId); // 게시글이 존재하는지 확인
        commentSvc.createComment(memberId, postId, commentDto); // 댓글 생성
        return ResponseDTO.of(HttpStatus.OK, "댓글을 생성했습니다.");
    }

}
