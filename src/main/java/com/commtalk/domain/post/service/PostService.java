package com.commtalk.domain.post.service;

import com.commtalk.domain.post.dto.PostDTO;
import com.commtalk.domain.post.dto.PostPreviewDTO;
import com.commtalk.domain.post.dto.request.PostCreateRequest;
import com.commtalk.domain.post.dto.PostPageDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostPageDTO getPostsByBoard(Long boardId, Pageable pageable);

    PostDTO getPost(Long postId);

    void isExistsPost(Long postId);

    List<PostPreviewDTO> getPostPreviewsByBoard(Long boardId, int size);

    List<PostPreviewDTO> getPostPreviewsByViews();

    void createPost(Long memberId, Long boardId, PostCreateRequest createReq);

}
