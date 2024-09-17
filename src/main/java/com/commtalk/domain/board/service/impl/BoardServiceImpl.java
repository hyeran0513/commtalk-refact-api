package com.commtalk.domain.board.service.impl;

import com.commtalk.common.exception.CustomException;
import com.commtalk.common.exception.EntityNotFoundException;
import com.commtalk.common.exception.ErrorCode;
import com.commtalk.domain.board.dto.BoardDTO;
import com.commtalk.domain.board.dto.BoardWithPinDTO;
import com.commtalk.domain.board.dto.PinnedBoardDTO;
import com.commtalk.domain.board.dto.request.BoardCreateRequest;
import com.commtalk.domain.board.entity.Board;
import com.commtalk.domain.board.entity.BoardRequest;
import com.commtalk.domain.board.entity.PinnedBoard;
import com.commtalk.domain.board.repository.BoardRepository;
import com.commtalk.domain.board.repository.BoardRequestRepository;
import com.commtalk.domain.board.repository.PinnedBoardRepository;
import com.commtalk.domain.board.service.BoardService;
import com.commtalk.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepo;
    private final BoardRequestRepository boardReqRepo;
    private final PinnedBoardRepository pinnedBoardRepo;

    @Override
    public List<BoardDTO> getAllBoard() {
        List<Board> boardList = boardRepo.findAll();

        return boardList.stream()
                .map(BoardDTO::from)
                .toList();
    }

    @Override
    public List<BoardWithPinDTO> getAllBoardWithPin(Long memberId) {
        // 전체 게시판, 핀고정 게시판 조회
        List<Board> boardList = boardRepo.findAll();
        List<PinnedBoard> pinnedBoardList = pinnedBoardRepo.findAllByMemberIdPinnedOrderByOrderAsc(memberId);

        // 핀고정 게시판을 boardId를 key로 하는 Map으로 변환
        Map<Long, PinnedBoard> pinnedBoardMap = pinnedBoardList.stream()
                .collect(Collectors.toMap(pinnedBoard -> pinnedBoard.getBoard().getId(), pinnedBoard -> pinnedBoard));

        // 전체 게시판에서 핀고정 게시판과 동일한 게시판의 경우 pinnedBoardId를 포함해서 반환
        return boardList.stream()
                .map(board -> {
                    BoardWithPinDTO boardWithPinDto = BoardWithPinDTO.from(board);
                    PinnedBoard pinnedBoard = pinnedBoardMap.get(board.getId());
                    if (pinnedBoard == null) {
                        boardWithPinDto.setPinnedBoardId(0L);
                    } else {
                        boardWithPinDto.setPinnedBoardId(pinnedBoard.getId());
                    }
                    return boardWithPinDto;
                })
                .toList();
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
                .map(PinnedBoardDTO::from)
                .toList();
    }

    @Override
    public void createBoardRequest(BoardCreateRequest createReq, Long memberId) {
        // 게시판 중복 여부 확인
        if (boardRepo.existsByBoardName(createReq.getBoardName())) {
            throw new CustomException(ErrorCode.DUPLICATE_BOARDNAME);
        }

        // 게시판 요청 생성
        Member requester = Member.builder().id(memberId).build();
        Member approver = Member.builder().id(1L).build(); // 관리자 ID
        BoardRequest boardReq = BoardRequest.create(createReq, requester, approver);
        BoardRequest newBoardReq = boardReqRepo.save(boardReq);
        if (newBoardReq.getId() == null) {
            throw new CustomException(ErrorCode.BOARD_CREATE_FAILED);
        }
    }

    @Override
    public void pinDefaultBoard(Long memberId) {
        // 기본 고정 게시판 지정 (게시판 시퀀스 1~4)
        List<Long> boardIds = LongStream.rangeClosed(1, 4).boxed().toList();
        pinBoards(memberId, boardIds);
    }

    @Transactional
    public void pinAndUnpinBoards(Long memberId, List<BoardWithPinDTO> pinReqList) {
        // 핀고정 해제
        unpinBoards(memberId, pinReqList.stream()
                .map(BoardWithPinDTO::getBoardId)
                .toList());

        // 핀고정
        pinBoards(memberId, pinReqList.stream()
                .filter(request -> request.getPinnedBoardId() == null || request.getPinnedBoardId() == 0)
                .map(BoardWithPinDTO::getBoardId)
                .toList());
    }

    @Override
    public void pinBoards(Long memberId, List<Long> pinBoardIds) {
        if (pinBoardIds != null && !pinBoardIds.isEmpty()) {
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
            for (Long boardId : pinBoardIds) {
                board = boardRepo.findById(boardId)
                        .orElseThrow(() -> new EntityNotFoundException("게시판을 찾을 수 없습니다."));

                PinnedBoard pinnedBoard = PinnedBoard.create(member, board, cnt++);
                pinnedBoardRepo.save(pinnedBoard);
            }

        }
    }

    @Override
    public void unpinBoards(Long memberId, List<Long> pinBoardIds) {
        if (pinBoardIds != null) {
            // 핀고정 게시판 삭제
            if (pinBoardIds.isEmpty()) {
                pinnedBoardRepo.deleteAllByMemberId(memberId);
            } else {
                pinnedBoardRepo.deleteByMemberIdAndBoardIdNotIn(memberId, pinBoardIds);
            }
        }
    }

    @Override
    @Transactional
    public void reorderPinnedBoards(Long memberId, List<Long> boardIdList) {
        int cnt = 0;
        for (Long boardId : boardIdList) {
            PinnedBoard pinnedBoard = pinnedBoardRepo.findByMemberIdAndBoardId(memberId, boardId)
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
