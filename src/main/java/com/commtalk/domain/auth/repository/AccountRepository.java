package com.commtalk.domain.auth.repository;

import com.commtalk.domain.auth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {

}
