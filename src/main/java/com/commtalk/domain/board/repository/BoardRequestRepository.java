package com.commtalk.domain.board.repository;

import com.commtalk.domain.board.entity.BoardRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRequestRepository extends JpaRepository<BoardRequest, Long> {

}
