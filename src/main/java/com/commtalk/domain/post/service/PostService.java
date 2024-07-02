package com.commtalk.domain.post.service;

import com.commtalk.domain.post.dto.CreatePostDTO;
import com.commtalk.domain.post.dto.PostPageDTO;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostPageDTO getPostsByBoard(Long boardId, Pageable pageable);

    void createPost(Long memberId, Long boardId, CreatePostDTO postDto);

}
