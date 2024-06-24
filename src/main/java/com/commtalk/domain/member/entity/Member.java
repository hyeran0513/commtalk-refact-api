package com.commtalk.domain.member.entity;

import com.commtalk.common.entity.BaseEntity;
import com.commtalk.domain.auth.dto.JoinDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    public static Member create(JoinDTO joinDTO) {
        return Member.builder()
                .memberName(joinDTO.getUsername())
                .email(joinDTO.getEmail())
                .phone(joinDTO.getPhone())
                .build();
    }

}