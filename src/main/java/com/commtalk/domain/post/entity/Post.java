package com.commtalk.domain.post.entity;

import com.commtalk.common.entity.BaseEntity;
import com.commtalk.domain.board.entity.Board;
import com.commtalk.domain.post.dto.request.PostCreateRequest;
import com.commtalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @Setter
    @Column(name = "post_title", nullable = false)
    private String title;

    @Setter
    @Column(name = "post_content", nullable = false)
    private String content;

    @Setter
    @Column(name = "anonymous_yn")
    private boolean anonymousYN;

    @Setter
    @Column(name = "commentable_yn")
    private boolean commentableYN;

    @Setter
    @Column(name = "deleted_yn")
    private boolean deletedYN;

    @Setter
    @Column(name = "view_count")
    private long viewCount;

    @Setter
    @Column(name = "like_count")
    private long likeCount;

    @Setter
    @Column(name = "scrap_count")
    private long scrapCount;

    public static Post create(Member member, Board board, PostCreateRequest createReq) {
        return Post.builder()
                .board(board)
                .author(member)
                .title(createReq.getTitle())
                .content(createReq.getContent())
                .anonymousYN(createReq.isAnonymousYN())
                .commentableYN(createReq.isCommentableYN())
                .viewCount(0)
                .likeCount(0)
                .scrapCount(0)
                .build();
    }

}
