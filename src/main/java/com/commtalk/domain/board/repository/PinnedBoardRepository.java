package com.commtalk.domain.board.repository;

import com.commtalk.domain.board.entity.Board;
import com.commtalk.domain.board.entity.PinnedBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinnedBoardRepository extends JpaRepository<PinnedBoard, Long> {

}
