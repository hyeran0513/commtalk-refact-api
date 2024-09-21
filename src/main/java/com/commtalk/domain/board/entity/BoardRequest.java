package com.commtalk.domain.board.entity;

import com.commtalk.common.entity.BaseEntity;
import com.commtalk.domain.board.dto.request.BoardCreateRequest;
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
@Table(name = "board_request")
public class BoardRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_req_id")
    private Long id;

    @Setter
    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private Member requester;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private Member approver;

    @Column(name = "board_name", nullable = false)
    private String boardName;

    @Column(name = "board_description")
    private String description;

    @Setter
    @Column(name = "req_sts")
    private int reqSts;

    @Setter
    @Column(name = "canceled_yn")
    private boolean canceledYN;

    public static BoardRequest create(BoardCreateRequest createReq, Member requester) {
        return BoardRequest.builder()
                .requester(requester)
                .boardName(createReq.getBoardName())
                .description(createReq.getDesc())
                .reqSts(0)
                .build();
    }

}
