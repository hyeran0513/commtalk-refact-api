package com.commtalk.domain.post.repository;

import com.commtalk.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT DISTINCT p FROM Post p " +
            "JOIN p.board b " +
            "JOIN FETCH p.author a " +
            "LEFT JOIN FETCH p.comments c " +
            "WHERE b.id = :boardId ORDER BY p.updatedAt DESC",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                    "JOIN p.board b " +
                    "WHERE b.id = :boardId")
    Page<Post> findByBoardIdOrderByUpdatedAt(Long boardId, Pageable pageable);

    @Query(value = "SELECT DISTINCT p FROM Post p " +
            "JOIN p.board b " +
            "LEFT JOIN FETCH p.comments c " +
            "WHERE b.id = :boardId ORDER BY p.viewCount DESC",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                    "JOIN p.board b " +
                    "WHERE b.id = :boardId")
    Page<Post> findByBoardIdOrderByViewCount(Long boardId, Pageable pageable);

}
