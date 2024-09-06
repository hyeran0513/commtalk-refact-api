package com.commtalk.domain.post.entity;

import com.commtalk.common.entity.BaseEntity;
import com.commtalk.domain.member.entity.Member;
import com.commtalk.domain.post.dto.request.CommentCreateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;

    @Setter
    @Column(name = "comment_content", nullable = false)
    private String content;

    @Setter
    @Column(name = "anonymous_yn")
    private boolean anonymousYN;

    @Setter
    @Column(name = "deleted_yn")
    private boolean deletedYN;

    @Setter
    @Column(name = "like_count")
    private long likeCount;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Transient
    @Setter
    private boolean skipUpdateAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        if (!this.skipUpdateAt) {
            this.updatedAt = Timestamp.valueOf(LocalDateTime.now()); // 새로운 날짜로 업데이트
        }
    }

    public static Comment create(Member member, Post post, CommentCreateRequest createReq) {
        return Comment.builder()
                .post(post)
                .writer(member)
                .content(createReq.getContent())
                .anonymousYN(createReq.isAnonymousYN())
                .likeCount(0)
                .build();
    }

}
