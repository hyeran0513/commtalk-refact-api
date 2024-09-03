package com.commtalk.domain.post.service;

import com.commtalk.domain.post.dto.request.CommentCreateRequest;
import com.commtalk.domain.post.dto.ParentCommentDTO;
import com.commtalk.domain.post.dto.request.CommentUpdateRequest;
import com.commtalk.domain.post.entity.Comment;

import java.util.List;

public interface CommentService {

    List<ParentCommentDTO> getCommentsByPost(Long postId);

    List<ParentCommentDTO> getCommentsByPost(Long postId, Long memberId);

    long getCommentCountByPost(Long postId);

    List<Long> getPostIdsByCommenter(Long memberId);

    void createComment(Long memberId, Long postId, CommentCreateRequest createReq);

    void updateComment(Long memberId, Long commentId, CommentUpdateRequest updateReq);

    void deleteComment(Long memberId, Long commentId);

}
