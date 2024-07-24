package com.commtalk.domain.post.service;

import com.commtalk.domain.post.entity.ActivityType;

public interface ActivityService {

    void likePost(Long postId, Long memberId);

    void cancelLikePost(Long postId, Long memberId);

    void scrapPost(Long postId, Long memberId);

    void cancelScrapPost(Long postId, Long memberId);

    void likeComment(Long commentId, Long memberId);

    void cancelLikeComment(Long commentId, Long memberId);

}
