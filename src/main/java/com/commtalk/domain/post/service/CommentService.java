package com.commtalk.domain.post.service;

import com.commtalk.domain.post.dto.request.CommentCreateRequest;
import com.commtalk.domain.post.dto.ParentCommentDTO;

import java.util.List;

public interface CommentService {

    List<ParentCommentDTO> getCommentsByPost(Long postId);

    void createComment(Long memberId, Long postId, CommentCreateRequest createReq);

}
