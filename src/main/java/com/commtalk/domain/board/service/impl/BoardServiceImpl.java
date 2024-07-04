package com.commtalk.domain.board.service.impl;

import com.commtalk.common.exception.EntityNotFoundException;
import com.commtalk.domain.board.dto.BoardDTO;
import com.commtalk.domain.board.dto.PinnedBoardDTO;
import com.commtalk.domain.board.entity.Board;
import com.commtalk.domain.board.entity.PinnedBoard;
import com.commtalk.domain.board.repository.BoardRepository;
import com.commtalk.domain.board.repository.PinnedBoardRepository;
import com.commtalk.domain.board.service.BoardService;
import com.commtalk.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new EntityNotFoundException("게시판을 찾을 수 없습니다."));

        return BoardDTO.from(board);
    }

    @Override
    public void isExistsBoard(Long boardId) {
        if (!boardRepo.existsById(boardId)) {
            throw new EntityNotFoundException("게시판을 찾을 수 없습니다.");
        }
    }

    @Override
    public List<PinnedBoardDTO> getPinnedBoards(Long memberId) {
        List<PinnedBoard> pinnedBoardList = pinnedBoardRepo.findAllByMemberIdPinnedOrderByOrderAsc(memberId);

        return pinnedBoardList.stream()
                .map(pinnedBoard -> PinnedBoardDTO.from(pinnedBoard.getBoard()))
                .collect(Collectors.toList());
    }

    @Override
    public void pinDefaultBoard(Long memberId) {
        // 기본 고정 게시판 지정 (게시판 시퀀스 1~4)
        List<Long> boardIds = LongStream.rangeClosed(1, 4).boxed().toList();
        pinBoards(memberId, boardIds);
    }

    @Override
    public void pinBoards(Long memberId, List<Long> boardIds) {
        if (boardIds != null && !boardIds.isEmpty()) {
            // 마지막 순서에 해당하는 핀고정 게시판 조회
            PinnedBoard lastPinnedBoard = pinnedBoardRepo.findFirstByMemberIdOrderByPinnedOrderDesc(memberId)
                    .orElse(null);

            // 순서의 시작점 지정
            int cnt = 0;
            if (lastPinnedBoard != null) {
                cnt = lastPinnedBoard.getPinnedOrder() + 1;
            }

            // 시작 순서부터 핀고정 게시판 생성
            Member member = Member.builder().id(memberId).build();
            Board board;
            for (Long boardId : boardIds) {
                board = boardRepo.findById(boardId)
                        .orElseThrow(() -> new EntityNotFoundException("게시판을 찾을 수 없습니다."));

                PinnedBoard pinnedBoard = PinnedBoard.create(member, board, cnt++);
                pinnedBoardRepo.save(pinnedBoard);
            }
        }
    }

    @Override
    public void unpinBoards(Long memberId, List<Long> boardIds) {
        if (boardIds != null && !boardIds.isEmpty()) {
            // 핀고정 게시판 삭제
            pinnedBoardRepo.deleteByMemberIdAndBoardIdIn(memberId, boardIds);
        }
    }

    @Override
    @Transactional
    public void reorderPinnedBoards(Long memberId, List<PinnedBoardDTO> boardDtoList) {
        int cnt = 0;
        for (PinnedBoardDTO boardDto : boardDtoList) {
            PinnedBoard pinnedBoard = pinnedBoardRepo.findByMemberIdAndBoardId(memberId, boardDto.getBoardId())
                    .orElseThrow(() -> new EntityNotFoundException("핀고정 게시판을 찾을 수 없습니다."));

            // 순서가 달라진 경우에만 DB 업데이트
            if (pinnedBoard.getPinnedOrder() != cnt) {
                pinnedBoard.setPinnedOrder(cnt);
                pinnedBoardRepo.save(pinnedBoard);
            }
            cnt++;
        }
    }

}
