package com.commtalk.domain.board.repository;

import com.commtalk.domain.board.entity.PinnedBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PinnedBoardRepository extends JpaRepository<PinnedBoard, Long> {

    @Query("SELECT pb FROM PinnedBoard pb JOIN FETCH pb.board WHERE pb.member.id = :memberId")
    List<PinnedBoard> findAllByMemberId(@Param("memberId") Long memberId);

}
