package com.commtalk.domain.post.repository;

import com.commtalk.domain.post.entity.ActivityType;
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

    @Query("SELECT c, CASE WHEN ma.id IS NOT NULL THEN TRUE ELSE FALSE END " +
            "FROM Comment c " +
            "LEFT JOIN MemberActivity ma ON c.id = ma.refId AND ma.member.id = :memberId AND ma.type.typeName = :typeName " +
            "WHERE c.post.id = :postId AND c.deletedYN = :deletedYN")
    List<Object[]> findByPostIdAndDeletedYN(Long postId, boolean deletedYN, Long memberId, ActivityType.TypeName typeName);

    Long countByPostIdAndDeletedYN(Long postId, boolean deletedYN);

}
