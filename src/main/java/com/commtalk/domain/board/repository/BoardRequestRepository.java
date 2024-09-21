package com.commtalk.domain.board.repository;

import com.commtalk.domain.board.entity.BoardRequest;
import com.commtalk.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRequestRepository extends JpaRepository<BoardRequest, Long> {

    @EntityGraph(attributePaths = {"requester", "approver"})
    Page<BoardRequest> findByOrderByUpdatedAtDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"requester", "approver"})
    Page<BoardRequest> findByRequesterIdOrderByUpdatedAtDesc(Long requesterId, Pageable pageable);

}
