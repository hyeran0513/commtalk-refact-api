package com.commtalk.domain.auth.repository;

import com.commtalk.domain.auth.entity.Account;
import com.commtalk.domain.auth.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {

    Optional<AccountRole> findByRoleName(AccountRole.Role roleName);

}
