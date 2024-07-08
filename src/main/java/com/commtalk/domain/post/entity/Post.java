package com.commtalk.domain.post.entity;

import com.commtalk.common.entity.BaseEntity;
import com.commtalk.domain.board.entity.Board;
import com.commtalk.domain.post.dto.CreatePostDTO;
import com.commtalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column(name = "post_title", nullable = false)
    private String title;

    @Column(name = "post_content", nullable = false)
    private String content;

    @Column(name = "anonymous_yn")
    private boolean anonymousYN;

    @Column(name = "commentable_yn")
    private boolean commentableYN;

    @Column(name = "deleted_yn")
    private boolean deletedYN;

    @Column(name = "view_count")
    private long viewCount;

    @Column(name = "like_count")
    private long likeCount;

    @Column(name = "scrap_count")
    private long scrapCount;

    public static Post create(Member member, Board board, CreatePostDTO postDto) {
        return Post.builder()
                .board(board)
                .author(member)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .anonymousYN(postDto.isAnonymousYN())
                .commentableYN(postDto.isCommentableYN())
                .viewCount(0)
                .likeCount(0)
                .scrapCount(0)
                .build();
    }

}
