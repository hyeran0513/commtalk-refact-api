package com.commtalk.domain.post.repository;

import com.commtalk.domain.post.entity.ActivityType;
import com.commtalk.domain.post.entity.MemberActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberActivityRepository extends JpaRepository<MemberActivity, Long> {

    Optional<MemberActivity> findByTypeIdAndMemberIdAndRefId(Long typeId, Long memberId, Long refId);

    boolean existsByTypeIdAndMemberIdAndRefId(Long typeId, Long memberId, Long refId);

    void deleteByTypeIdAndMemberIdAndRefId(Long typeId, Long memberId, Long refId);

    void deleteAllByTypeIdAndRefId(Long typeId, Long refId);

}
