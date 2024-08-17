package com.commtalk.domain.board.repository;

import com.commtalk.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    boolean existsByBoardName(String boardName);

}
