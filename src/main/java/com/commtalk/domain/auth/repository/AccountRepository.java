package com.commtalk.domain.auth.repository;

import com.commtalk.domain.auth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByNickname(String nickname);
    Optional<Account> findByNickname(String nickname);

}
