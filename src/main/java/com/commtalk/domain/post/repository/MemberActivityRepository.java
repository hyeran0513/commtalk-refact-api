package com.commtalk.domain.post.repository;

import com.commtalk.domain.post.entity.ActivityType;
import com.commtalk.domain.post.entity.MemberActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberActivityRepository extends JpaRepository<MemberActivity, Long> {

    Optional<MemberActivity> findByMemberIdAndRefIdAndTypeName(Long memberId, Long refId, ActivityType.TypeName typeName);

    boolean existsByMemberIdAndRefIdAndTypeName(Long memberId, Long refId, ActivityType.TypeName typeName);

    void deleteByMemberIdAndRefIdAndTypeName(Long memberId, Long refId, ActivityType.TypeName typeName);

    void deleteAllByRefIdAndTypeName(Long refId, ActivityType.TypeName typeName);

    @Query(value = "SELECT DISTINCT ma.refId FROM MemberActivity ma " +
            "JOIN ma.member m " +
            "WHERE m.id = :memberId AND ma.type.name = :typeName")
    List<Long> getRefIdsByMemberIdAndTypeName(Long memberId, ActivityType.TypeName typeName);

}
