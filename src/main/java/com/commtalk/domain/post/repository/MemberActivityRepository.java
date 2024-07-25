package com.commtalk.domain.post.repository;

import com.commtalk.domain.post.entity.ActivityType;
import com.commtalk.domain.post.entity.MemberActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberActivityRepository extends JpaRepository<MemberActivity, Long> {

    List<MemberActivity> findByMemberIdAndPostId(Long memberId, Long postId);

    List<MemberActivity> findByMemberIdAndCommentId(Long memberId, Long commentId);

    void deleteByMemberIdAndPostIdAndType(Long memberId, Long postId, ActivityType type);

    void deleteByMemberIdAndCommentIdAndType(Long memberId, Long commentId, ActivityType type);

}
