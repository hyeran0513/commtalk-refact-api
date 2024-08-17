package com.commtalk.domain.member.repository;

import com.commtalk.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByNicknameAndDeletedYN(String nickname, Boolean deletedYN);

    @Query("SELECT m FROM Member m JOIN FETCH m.password mp " +
            "WHERE m.nickname = :nickname AND m.deletedYN = :deletedYN")
    Optional<Member> findByNickName(String nickname, boolean deletedYN);

}
