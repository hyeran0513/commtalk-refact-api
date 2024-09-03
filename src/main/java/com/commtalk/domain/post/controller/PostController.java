package com.commtalk.domain.post.controller;

import com.commtalk.domain.post.dto.PostPageDTO;
import com.commtalk.domain.post.dto.PostPreviewDTO;
import com.commtalk.domain.post.entity.ActivityType;
import com.commtalk.domain.post.service.CommentService;
import com.commtalk.domain.post.service.MemberActivityService;
import com.commtalk.domain.post.service.PostService;
import com.commtalk.security.JwtAuthenticationProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
    private final MemberActivityService memberActivitySvc;

    @Operation(summary = "게시글 목록 조회")
    @GetMapping(path = "")
    public ResponseEntity<PostPageDTO> getPostsByKeyword(@RequestParam(required = false) String keyword, @PageableDefault Pageable pageable) {
        PostPageDTO postPageDto = (keyword == null) ? postSvc.getPosts(pageable)
                : postSvc.getPostsByKeyword(keyword, pageable); // 게시글 목록 조회
        postPageDto.getPosts()
                .forEach(p -> p.setCommentCnt(commentSvc.getCommentCountByPost(p.getPostId()))); // 게시글 댓글 수 조회
        return ResponseEntity.ok(postPageDto);
    }

    @Operation(summary = "회원 작성 게시글 목록 조회")
    @GetMapping(path = "/me/posted")
    public ResponseEntity<PostPageDTO> getPostsByAuthor(@PageableDefault Pageable pageable, HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        PostPageDTO postPageDto = postSvc.getPostsByAuthor(memberId, pageable); // 게시글 목록 조회
        postPageDto.getPosts()
                .forEach(p -> p.setCommentCnt(commentSvc.getCommentCountByPost(p.getPostId()))); // 게시글 댓글 수 조회
        return ResponseEntity.ok(postPageDto);
    }

    @Operation(summary = "회원 댓글 작성 게시글 목록 조회")
    @GetMapping(path = "/me/commented")
    public ResponseEntity<PostPageDTO> getPostsByCommenter(@PageableDefault Pageable pageable, HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        List<Long> postIds = commentSvc.getPostIdsByCommenter(memberId);
        PostPageDTO postPageDto = postSvc.getPostsByIds(postIds, pageable); // 게시글 목록 조회
        postPageDto.getPosts()
                .forEach(p -> p.setCommentCnt(commentSvc.getCommentCountByPost(p.getPostId()))); // 게시글 댓글 수 조회
        return ResponseEntity.ok(postPageDto);
    }

    @Operation(summary = "회원 좋아요 게시글 목록 조회")
    @GetMapping(path = "/me/liked")
    public ResponseEntity<PostPageDTO> getPostsByLike(@PageableDefault Pageable pageable, HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        List<Long> postIds = memberActivitySvc.getPostIdsByLikeOrScrap(memberId, ActivityType.TypeName.POST_LIKE);
        PostPageDTO postPageDto = postSvc.getPostsByIds(postIds, pageable); // 게시글 목록 조회
        postPageDto.getPosts()
                .forEach(p -> p.setCommentCnt(commentSvc.getCommentCountByPost(p.getPostId()))); // 게시글 댓글 수 조회
        return ResponseEntity.ok(postPageDto);
    }

    @Operation(summary = "회원 스크랩 게시글 목록 조회")
    @GetMapping(path = "/me/scrapped")
    public ResponseEntity<PostPageDTO> getPostsByScrap(@PageableDefault Pageable pageable, HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        List<Long> postIds = memberActivitySvc.getPostIdsByLikeOrScrap(memberId, ActivityType.TypeName.POST_SCRAP);
        PostPageDTO postPageDto = postSvc.getPostsByIds(postIds, pageable); // 게시글 목록 조회
        postPageDto.getPosts()
                .forEach(p -> p.setCommentCnt(commentSvc.getCommentCountByPost(p.getPostId()))); // 게시글 댓글 수 조회
        return ResponseEntity.ok(postPageDto);
    }

    @Operation(summary = "인기 게시글 목록 조회")
    @GetMapping(path = "/popular")
    public ResponseEntity<List<PostPreviewDTO>> getPostsByViews() {
        List<PostPreviewDTO> postPreviewDtoList = postSvc.getPostPreviewsTop3ByViews(); // 게시글 목록 조회
        postPreviewDtoList
                .forEach(p -> p.setCommentCnt(commentSvc.getCommentCountByPost(p.getPostId()))); // 게시글 댓글 수 조회
        return ResponseEntity.ok(postPreviewDtoList);
    }

}
