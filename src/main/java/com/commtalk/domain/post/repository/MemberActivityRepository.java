package com.commtalk.domain.post.repository;

import com.commtalk.domain.post.entity.ActivityType;
import com.commtalk.domain.post.entity.MemberActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberActivityRepository extends JpaRepository<MemberActivity, Long> {

    Optional<MemberActivity> findByMemberIdAndRefIdAndTypeName(Long memberId, Long refId, ActivityType.TypeName typeName);

    boolean existsByMemberIdAndRefIdAndTypeName(Long memberId, Long refId, ActivityType.TypeName typeName);

    void deleteByMemberIdAndRefIdAndTypeName(Long memberId, Long refId, ActivityType.TypeName typeName);

    void deleteAllByRefIdAndTypeName(Long refId, ActivityType.TypeName typeName);

}
