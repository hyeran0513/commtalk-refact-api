package com.commtalk.domain.member.entity;

import com.commtalk.common.entity.BaseEntity;
import com.commtalk.domain.member.dto.JoinDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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

    @Setter
    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    private String phone;

    public static Member create(JoinDTO joinDto) {
        return Member.builder()
                .memberName(joinDto.getUsername())
                .email(joinDto.getEmail())
                .phone(joinDto.getPhone())
                .build();
    }

}