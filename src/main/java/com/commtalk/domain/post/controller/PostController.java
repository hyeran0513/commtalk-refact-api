package com.commtalk.domain.post.controller;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.post.dto.PostPageDTO;
import com.commtalk.domain.post.dto.request.CommentCreateRequest;
import com.commtalk.domain.post.dto.ParentCommentDTO;
import com.commtalk.domain.post.dto.PostPreviewDTO;
import com.commtalk.domain.post.dto.request.CommentUpdateRequest;
import com.commtalk.domain.post.service.CommentService;
import com.commtalk.domain.post.service.PostService;
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

    @Operation(summary = "게시글 목록 조회")
    @GetMapping(path = "")
    public ResponseEntity<PostPageDTO> getPostsByKeyword(@RequestParam(required = false) String keyword, @PageableDefault Pageable pageable) {
        PostPageDTO postPageDto = (keyword == null) ? postSvc.getPosts(pageable)
                : postSvc.getPostsByKeyword(keyword, pageable); // 게시글 목록 조회
        postPageDto.getPosts()
                .forEach(p -> p.setCommentCnt(commentSvc.getCommentCountByPost(p.getPostId()))); // 게시글 댓글 수 조회
        return ResponseEntity.ok(postPageDto);
    }

    @Operation(summary = "인기 게시글 목록 조회")
    @GetMapping(path = "/popular")
    public ResponseEntity<List<PostPreviewDTO>> getPostsByViews() {
        List<PostPreviewDTO> postPreviewDtoList = postSvc.getPostPreviewsByViews(); // 게시글 목록 조회
        postPreviewDtoList
                .forEach(p -> p.setCommentCnt(commentSvc.getCommentCountByPost(p.getPostId()))); // 게시글 댓글 수 조회
        return ResponseEntity.ok(postPreviewDtoList);
    }

    @Operation(summary = "게시글 댓글 목록 조회")
    @GetMapping(path = "/{postId}/comments")
    public ResponseEntity<List<ParentCommentDTO>> getCommentsByPost(@PathVariable Long postId) {
        postSvc.isExistsPost(postId); // 게시글이 존재하는지 확인
        List<ParentCommentDTO> commentDtoList = commentSvc.getCommentsByPost(postId); // 게시글 댓글 목록 조회
        return ResponseEntity.ok(commentDtoList);
    }


    @Operation(summary = "게시글 댓글 생성")
    @PostMapping(path = "/{postId}/comments")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseDTO<String>> createComment(@PathVariable Long postId, @RequestBody @Valid CommentCreateRequest createReq,
                                                             HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        postSvc.isExistsPost(postId); // 게시글이 존재하는지 확인
        commentSvc.createComment(memberId, postId, createReq); // 댓글 생성
        return ResponseDTO.of(HttpStatus.OK, "댓글을 생성했습니다.");
    }

    @Operation(summary = "게시글 댓글 수정")
    @PatchMapping(path = "/{postId}/comments/{commentId}")
    public ResponseEntity<ResponseDTO<String>> updateComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                             @RequestBody @Valid CommentUpdateRequest updateReq, HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        postSvc.isExistsPost(postId); // 게시글이 존재하는지 확인
        commentSvc.updateComment(memberId, commentId, updateReq); // 댓글 수정
        return ResponseDTO.of(HttpStatus.OK, "댓글을 수정했습니다.");
    }

    @Operation(summary = "게시글 댓글 삭제")
    @DeleteMapping(path = "/{postId}/comments/{commentId}")
    public ResponseEntity<ResponseDTO<String>> deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                             HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        postSvc.isExistsPost(postId); // 게시글이 존재하는지 확인
        commentSvc.deleteComment(memberId, commentId); // 댓글 수정
        return ResponseDTO.of(HttpStatus.OK, "댓글을 삭제했습니다.");
    }

}
