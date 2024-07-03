package com.commtalk.domain.post.service;

import com.commtalk.domain.post.dto.PostPreviewDTO;
import com.commtalk.domain.post.dto.CreatePostDTO;
import com.commtalk.domain.post.dto.PostPageDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostPageDTO getPostsByBoard(Long boardId, Pageable pageable);

    List<PostPreviewDTO> getPostPreviewsByBoard(Long boardId, int size);

    void createPost(Long memberId, Long boardId, CreatePostDTO postDto);

}
