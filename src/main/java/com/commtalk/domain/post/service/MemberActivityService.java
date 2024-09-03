package com.commtalk.domain.post.service;

import com.commtalk.domain.post.dto.ParentCommentDTO;
import com.commtalk.domain.post.dto.PostDTO;
import com.commtalk.domain.post.entity.ActivityType;

import java.util.List;

public interface MemberActivityService {

    boolean isLikeOrScrapPost(Long memberId, Long postId, ActivityType.TypeName typeName);

    PostDTO likeOrScrapPost(Long memberId, Long postId, ActivityType.TypeName typeName);

    PostDTO unlikeOrScrapPost(Long memberId, Long postId, ActivityType.TypeName typeName);

    boolean isLikeComment(Long memberId, Long commentId);

    ParentCommentDTO likeComment(Long memberId, Long commentId);

    ParentCommentDTO unlikeComment(Long memberId, Long commentId);

    List<Long> getPostIdsByLikeOrScrap(Long memberId, ActivityType.TypeName typeName);

}
