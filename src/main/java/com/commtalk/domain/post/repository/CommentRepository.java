package com.commtalk.domain.post.repository;

import com.commtalk.domain.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long postId);

    Long countByPostId(Long postId);

}
