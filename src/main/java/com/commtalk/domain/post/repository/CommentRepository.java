package com.commtalk.domain.post.repository;

import com.commtalk.domain.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT DISTINCT c FROM Comment c " +
            "JOIN FETCH c.writer w " +
            "WHERE c.id = :commentId")
    Optional<Comment> findByIdWithWriter(Long commentId);

    List<Comment> findByPostIdAndDeletedYN(Long postId, boolean deletedYN);

    Long countByPostIdAndDeletedYN(Long postId, boolean deletedYN);

}
