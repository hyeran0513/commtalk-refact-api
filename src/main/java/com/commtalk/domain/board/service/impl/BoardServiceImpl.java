package com.commtalk.domain.board.service.impl;

import com.commtalk.domain.board.entity.Board;
import com.commtalk.domain.board.entity.PinnedBoard;
import com.commtalk.domain.board.repository.BoardRepository;
import com.commtalk.domain.board.repository.PinnedBoardRepository;
import com.commtalk.domain.board.service.BoardService;
import com.commtalk.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepo;
    private final PinnedBoardRepository pinnedBoardRepo;

    @Override
    public void pinDefaultBoardByMember(Long memberId) {
        // 기본 고정 게시판 지정 (게시판 시퀀스 0~3)
        List<Long> boardIds = LongStream.range(0, 4).boxed().toList();
        Member member = Member.builder().id(memberId).build();
        Board board;

        int cnt = 0;
        for (Long boardId : boardIds) {
            if (boardRepo.existsById(boardId)) {
                board = Board.builder().id(boardId).build();
                PinnedBoard pinnedBoard = PinnedBoard.create(member, board, cnt++);
                pinnedBoardRepo.save(pinnedBoard);
            }
        }
    }

}
