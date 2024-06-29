package com.commtalk.domain.member.repository;

import com.commtalk.domain.member.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {

    Optional<AccountRole> findByRoleName(AccountRole.Role roleName);

}
