package com.commtalk.domain.post.entity;

import com.commtalk.common.entity.BaseEntity;
import com.commtalk.domain.member.entity.Member;
import com.commtalk.domain.post.dto.request.CommentCreateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

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

    @Column(name = "comment_content", nullable = false)
    private String content;

    @Column(name = "anonymous_yn")
    private boolean anonymousYN;

    @Column(name = "deleted_yn")
    private boolean deletedYN;

    @Column(name = "like_count")
    private long likeCount;

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
