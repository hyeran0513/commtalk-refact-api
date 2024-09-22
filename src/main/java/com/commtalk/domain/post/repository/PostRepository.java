package com.commtalk.domain.post.repository;

import com.commtalk.domain.post.entity.ActivityType;
import com.commtalk.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT DISTINCT p, ma_l.id, ma_s.id FROM Post p " +
            "LEFT JOIN MemberActivity ma_l ON p.id = ma_l.refId AND ma_l.member.id = :memberId AND ma_l.type.name = :lTypeName " +
            "LEFT JOIN MemberActivity ma_s ON p.id = ma_s.refId AND ma_s.member.id = :memberId AND ma_s.type.name = :sTypeName " +
            "WHERE p.id = :id AND p.deletedYN = :deletedYN")
    Optional<Object[]> findById(Long id, Long memberId, ActivityType.TypeName lTypeName,
                                ActivityType.TypeName sTypeName, boolean deletedYN);

    @EntityGraph(attributePaths = {"board", "author"})
    Page<Post> findByDeletedYNOrderByUpdatedAtDesc(boolean deletedYN, Pageable pageable);

    @EntityGraph(attributePaths = {"board", "author"})
    Page<Post> findByBoardIdAndDeletedYNOrderByUpdatedAtDesc(Long boardId, boolean deletedYN, Pageable pageable);

    @EntityGraph(attributePaths = {"board", "author"})
    Page<Post> findByBoardIdAndDeletedYNOrderByViewCountDesc(Long boardId, boolean deletedYN, Pageable pageable);

    @EntityGraph(attributePaths = {"board", "author"})
    Page<Post> findByTitleContainingOrContentContainingAndDeletedYNOrderByUpdatedAtDesc(
            String titleKeyword, String contentKeyword, boolean deletedYN, Pageable pageable);

    @EntityGraph(attributePaths = {"board", "author"})
    Page<Post> findByBoardIdAndTitleContainingOrContentContainingAndDeletedYNOrderByUpdatedAtDesc(
            Long boardId, String titleKeyword, String contentKeyword, boolean deletedYN, Pageable pageable);

    @EntityGraph(attributePaths = {"board", "author"})
    Page<Post> findByAuthorIdAndDeletedYNOrderByUpdatedAtDesc(Long authorId, boolean deletedYN, Pageable pageable);

    @EntityGraph(attributePaths = {"board", "author"})
    Page<Post> findByIdInAndDeletedYNOrderByUpdatedAtDesc(List<Long> postIds, boolean deletedYN, Pageable pageable);

    @EntityGraph(attributePaths = {"board", "author"})
    Page<Post> findByDeletedYNOrderByViewCountDesc(boolean deletedYN, Pageable pageable);

    int countByBoardIdAndDeletedYN(Long boardId, boolean deletedYN);

}
