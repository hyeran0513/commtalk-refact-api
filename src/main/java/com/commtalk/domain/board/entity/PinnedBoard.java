package com.commtalk.domain.board.entity;

import com.commtalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "pinned_board")
public class PinnedBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pinned_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "pinned_order", nullable = false)
    private int pinnedOrder;

    public static PinnedBoard create(Member member, Board board, int order) {
        return PinnedBoard.builder()
                .member(member)
                .board(board)
                .pinnedOrder(order)
                .build();
    }

}
