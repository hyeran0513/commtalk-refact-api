package com.commtalk.domain.member.repository;

import com.commtalk.domain.member.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

    Optional<MemberRole> findByRoleName(MemberRole.RoleName roleName);

}
