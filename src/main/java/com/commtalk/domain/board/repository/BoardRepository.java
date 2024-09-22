package com.commtalk.domain.board.repository;

import com.commtalk.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByDeletedYNOrderById(boolean deletedYN);

    boolean existsByBoardNameAndDeletedYN(String boardName, boolean deletedYN);

}
