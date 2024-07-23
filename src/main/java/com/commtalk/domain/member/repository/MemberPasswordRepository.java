package com.commtalk.domain.member.repository;

import com.commtalk.domain.member.entity.MemberPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberPasswordRepository extends JpaRepository<MemberPassword, Long> {

    Optional<MemberPassword> findByMemberId(Long memberId);

}
