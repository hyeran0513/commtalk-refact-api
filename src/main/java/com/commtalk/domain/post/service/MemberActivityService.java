package com.commtalk.domain.post.service;

import com.commtalk.domain.post.dto.MemberLikeDTO;
import com.commtalk.domain.post.dto.MemberScrapDTO;
import com.commtalk.domain.post.entity.ActivityType;

import java.util.List;

public interface MemberActivityService {

    boolean isLikeOrScrapPost(Long memberId, Long postId, ActivityType.TypeName typeName);

    List<Long> getPostIdsByLikeOrScrap(Long memberId, ActivityType.TypeName typeName);

    MemberLikeDTO likePost(Long memberId, Long postId, int signNum);

    MemberScrapDTO scrapPost(Long memberId, Long postId, int signNum);

    boolean isLikeComment(Long memberId, Long commentId);

    MemberLikeDTO likeComment(Long memberId, Long commentId, int signNum);

}
