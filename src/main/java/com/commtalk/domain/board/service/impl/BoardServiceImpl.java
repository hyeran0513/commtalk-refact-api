package com.commtalk.domain.board.service.impl;

import com.commtalk.domain.board.dto.BoardDTO;
import com.commtalk.domain.board.dto.PinnedBoardDTO;
import com.commtalk.domain.board.entity.Board;
import com.commtalk.domain.board.entity.PinnedBoard;
import com.commtalk.domain.board.exception.BoardNotFoundException;
import com.commtalk.domain.board.repository.BoardRepository;
import com.commtalk.domain.board.repository.PinnedBoardRepository;
import com.commtalk.domain.board.service.BoardService;
import com.commtalk.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepo;
    private final PinnedBoardRepository pinnedBoardRepo;

    @Override
    public List<BoardDTO> getAllBoard() {
        List<Board> boardList = boardRepo.findAll();

        return boardList.stream()
                .map(BoardDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public BoardDTO getBoard(Long boardId) {
        Board board = boardRepo.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시판을 찾을 수 없습니다."));

        return BoardDTO.from(board);
    }

    @Override
    public List<PinnedBoardDTO> getPinnedBoards(Long memberId) {
        List<PinnedBoard> pinnedBoardList = pinnedBoardRepo.findAllByMemberId(memberId);

        return pinnedBoardList.stream()
                .map(PinnedBoardDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public void pinDefaultBoardByMember(Long memberId) {
        // 기본 고정 게시판 지정 (게시판 시퀀스 1~4)
        List<Long> boardIds = LongStream.rangeClosed(1, 4).boxed().toList();
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
