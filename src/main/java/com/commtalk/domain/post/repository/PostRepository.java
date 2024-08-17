package com.commtalk.domain.post.repository;

import com.commtalk.domain.post.entity.ActivityType;
import com.commtalk.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT DISTINCT p, ma_l.id, ma_s.id FROM Post p " +
            "LEFT JOIN MemberActivity ma_l ON p.id = ma_l.refId AND ma_l.member.id = :memberId AND ma_l.type.name = :lTypeName " +
            "LEFT JOIN MemberActivity ma_s ON p.id = ma_s.refId AND ma_s.member.id = :memberId AND ma_s.type.name = :sTypeName " +
            "WHERE p.id = :id AND p.deletedYN = :deletedYN")
    Optional<Object[]> findById(Long id, Long memberId, ActivityType.TypeName lTypeName,
                                ActivityType.TypeName sTypeName, boolean deletedYN);

    @Query(value = "SELECT DISTINCT p FROM Post p " +
            "JOIN FETCH p.author a " +
            "WHERE p.deletedYN = :deletedYN " +
            "ORDER BY p.updatedAt DESC",
            countQuery = "SELECT COUNT(p) FROM Post p WHERE p.deletedYN = :deletedYN")
    Page<Post> findAllOrderByUpdatedAt(Pageable pageable, boolean deletedYN);

    @Query(value = "SELECT DISTINCT p FROM Post p " +
            "JOIN p.board b " +
            "JOIN FETCH p.author a " +
            "WHERE b.id = :boardId AND p.deletedYN = :deletedYN " +
            "ORDER BY p.updatedAt DESC",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                    "JOIN p.board b " +
                    "WHERE b.id = :boardId AND p.deletedYN = :deletedYN")
    Page<Post> findByBoardIdOrderByUpdatedAt(Long boardId, Pageable pageable, boolean deletedYN);

    @Query(value = "SELECT DISTINCT p FROM Post p " +
            "JOIN FETCH p.author a " +
            "WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% AND p.deletedYN = :deletedYN " +
            "ORDER BY p.updatedAt DESC",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                    "WHERE (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) AND p.deletedYN = :deletedYN")
    Page<Post> findByKeywordOrderByUpdateAt(String keyword, Pageable pageable, boolean deletedYN);

    @Query(value = "SELECT DISTINCT p FROM Post p " +
            "JOIN p.board b " +
            "JOIN FETCH p.author a " +
            "WHERE b.id = :boardId AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) AND p.deletedYN = :deletedYN " +
            "ORDER BY p.updatedAt DESC",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                    "JOIN p.board b " +
                    "WHERE b.id = :boardId AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) AND p.deletedYN = :deletedYN")
    Page<Post> findByBoardAndKeywordOrderByUpdateAt(Long boardId, String keyword, Pageable pageable, boolean deletedYN);

    @Query(value = "SELECT DISTINCT p FROM Post p " +
            "JOIN FETCH p.board b " +
            "WHERE b.id = :boardId AND p.deletedYN = :deletedYN " +
            "ORDER BY p.viewCount DESC",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                    "JOIN p.board b " +
                    "WHERE b.id = :boardId AND p.deletedYN = :deletedYN")
    Page<Post> findByBoardIdOrderByViewCount(Long boardId, Pageable pageable, boolean deletedYN);

    Page<Post> findAllByDeletedYNOrderByViewCountDesc(Pageable pageable, boolean deletedYN);

}
