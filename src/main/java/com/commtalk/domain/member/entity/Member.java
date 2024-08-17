package com.commtalk.domain.member.entity;

import com.commtalk.common.entity.BaseEntity;
import com.commtalk.domain.member.dto.request.MemberJoinRequest;
import jakarta.persistence.*;

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

    @Column(nullable = false)
    private String nickname;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private MemberPassword password;

    @Setter
    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    private String phone;

    @Setter
    @Column(name = "deleted_yn")
    private boolean deletedYN;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_role_id", nullable = false)
    private MemberRole role;

    public static Member create(MemberJoinRequest joinReq, MemberRole role) {
        return Member.builder()
                .nickname(joinReq.getNickname())
                .memberName(joinReq.getUsername())
                .email(joinReq.getEmail())
                .phone(joinReq.getPhone())
                .role(role)
                .build();
    }

}