package com.commtalk.domain.member.entity;

import com.commtalk.common.entity.BaseEntity;
import com.commtalk.domain.member.dto.request.MemberJoinRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "member_password")
public class MemberPassword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "password_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String password;

    public static MemberPassword create(MemberJoinRequest joinReq, Member member, BCryptPasswordEncoder passwordEncoder) {
        return MemberPassword.builder()
                .member(member)
                .password(passwordEncoder.encode(joinReq.getPassword()))
                .build();
    }

    public boolean isEqualPassword(String password, BCryptPasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }

    public void setNewPassword(String newPassword, BCryptPasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(newPassword);
    }

}
