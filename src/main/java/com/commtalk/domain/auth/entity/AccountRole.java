package com.commtalk.domain.auth.entity;

import com.commtalk.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "account_role")
public class AccountRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_role_id")
    private Long id;

    @Column(name = "role_name", nullable = false)
    private String roleName;

}
