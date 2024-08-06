package com.commtalk.domain.post.service;

import com.commtalk.domain.post.dto.request.CommentCreateRequest;
import com.commtalk.domain.post.dto.ParentCommentDTO;
import com.commtalk.domain.post.dto.request.CommentUpdateRequest;

import java.util.List;

public interface CommentService {

    List<ParentCommentDTO> getCommentsByPost(Long postId);

    List<ParentCommentDTO> getCommentsByPost(Long postId, Long memberId);

    long getCommentCountByPost(Long postId);

    void createComment(Long memberId, Long postId, CommentCreateRequest createReq);

    void updateComment(Long memberId, Long commentId, CommentUpdateRequest updateReq);

    void deleteComment(Long memberId, Long commentId);

    boolean isLikeComment(Long memberId, Long commentId);

    void likeComment(Long memberId, Long commentId);

    void unlikeComment(Long memberId, Long commentId);

}
