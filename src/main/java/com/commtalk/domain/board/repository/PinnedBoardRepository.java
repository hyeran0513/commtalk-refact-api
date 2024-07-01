package com.commtalk.domain.board.repository;

import com.commtalk.domain.board.entity.PinnedBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PinnedBoardRepository extends JpaRepository<PinnedBoard, Long> {

    @Query("SELECT pb FROM PinnedBoard pb JOIN FETCH pb.board WHERE pb.member.id = :memberId ORDER BY pb.pinnedOrder ASC")
    List<PinnedBoard> findAllByMemberIdPinnedOrderByOrderAsc(@Param("memberId") Long memberId);

    Optional<PinnedBoard> findFirstByMemberIdOrderByPinnedOrderDesc(Long memberId);

    Optional<PinnedBoard> findByMemberIdAndBoardId(Long memberId, Long boardId);

    void deleteByMemberIdAndBoardIdIn(Long memberId, List<Long> boardIds);

}
