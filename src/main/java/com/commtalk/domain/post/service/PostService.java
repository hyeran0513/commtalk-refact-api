package com.commtalk.domain.post.service;

import com.commtalk.domain.post.dto.PostDTO;
import com.commtalk.domain.post.dto.PostPreviewDTO;
import com.commtalk.domain.post.dto.request.PostCreateRequest;
import com.commtalk.domain.post.dto.PostPageDTO;
import com.commtalk.domain.post.dto.request.PostUpdateRequest;
import com.commtalk.domain.post.entity.ActivityType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostPageDTO getPosts(Pageable pageable);

    PostPageDTO getPostsByBoard(Long boardId, Pageable pageable);

    PostPageDTO getPostsByKeyword(String keyword, Pageable pageable);

    PostPageDTO getPostsByBoardAndKeyword(Long boardId, String keyword, Pageable pageable);

    PostDTO getPost(Long postId);

    PostDTO getPost(Long postId, Long memberId);

    void isExistsPost(Long postId);

    List<PostPreviewDTO> getPostPreviewsByBoard(Long boardId, int size);

    List<PostPreviewDTO> getPostPreviewsByViews();

    Long createPost(Long memberId, Long boardId, PostCreateRequest createReq);

    void updatePost(Long memberId, Long postId, PostUpdateRequest updateReq);

    void deletePost(Long memberId, Long postId);

    boolean isLikeOrScrapPost(Long memberId, Long postId, ActivityType.TypeName typeName);

    void likeOrScrapPost(Long memberId, Long postId, ActivityType.TypeName typeName);

    void unlikeOrScrapPost(Long memberId, Long postId, ActivityType.TypeName typeName);

}
