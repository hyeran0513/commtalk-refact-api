package com.commtalk.domain.board.entity;

import com.commtalk.common.entity.BaseEntity;
import com.commtalk.domain.board.dto.request.BoardCreateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "board")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "manager_id", nullable = false)
    private Long managerId;

    @Column(name = "board_name", nullable = false)
    private String boardName;

    @Column(name = "board_description")
    private String description;

    public static Board create(BoardCreateRequest createReq, Long adminId) {
        return Board.builder()
                .boardName(createReq.getBoardName())
                .description(createReq.getDesc())
                .managerId(adminId)
                .build();
    }

}
