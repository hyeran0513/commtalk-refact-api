package com.commtalk.domain.post.entity;

import com.commtalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member_activity")
public class MemberActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_type_id", nullable = false)
    private ActivityType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "ref_id", nullable = false)
    private Long refId;

    @Column(name = "created_at")
    @CreatedDate
    private Timestamp createdAt;

    public static MemberActivity create(ActivityType type, Member member, Long refId) {
        return MemberActivity.builder()
                .type(type)
                .member(member)
                .refId(refId)
                .build();
    }

}
