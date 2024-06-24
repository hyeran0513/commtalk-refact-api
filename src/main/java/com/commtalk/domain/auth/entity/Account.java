package com.commtalk.domain.auth.entity;

import com.commtalk.common.entity.BaseEntity;
import com.commtalk.domain.auth.dto.JoinDTO;
import com.commtalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "account")
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_role_id", nullable = false)
    private AccountRole role;

    public static Account create(JoinDTO joinDTO, Long memberId, BCryptPasswordEncoder passwordEncoder) {
        AccountRole role = AccountRole.builder()
                .roleName(AccountRole.Role.ROLE_USER)
                .build();

        return Account.builder()
                .memberId(memberId)
                .nickname(joinDTO.getNickname())
                .password(passwordEncoder.encode(joinDTO.getPassword()))
                .role(role)
                .build();
    }

}
