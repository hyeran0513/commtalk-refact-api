package com.commtalk.domain.board.service.impl;

import com.commtalk.common.exception.CustomException;
import com.commtalk.common.exception.EntityNotFoundException;
import com.commtalk.common.exception.ErrorCode;
import com.commtalk.domain.board.dto.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.commtalk.common.exception.ErrorCode.MISMATCH_REQUESTER_ID;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepo;
    private final BoardRequestRepository boardReqRepo;
    private final PinnedBoardRepository pinnedBoardRepo;

    @Override
    public List<BoardDTO> getAllBoard() {
        List<Board> boardList = boardRepo.findByDeletedYNOrderById(false);

        return boardList.stream()
                .map(BoardDTO::from)
                .toList();
    }

    @Override
    public List<BoardWithPinDTO> getAllBoardWithPin(Long memberId) {
        // 전체 게시판, 핀고정 게시판 조회
        List<Board> boardList = boardRepo.findByDeletedYNOrderById(false);
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
    public BoardRequestPageDTO getAllBoardRequest(Pageable pageable) {
        Page<BoardRequest> boardReqPage = boardReqRepo.findByOrderByUpdatedAtDesc(pageable);
        return BoardRequestPageDTO.of(boardReqPage);
    }

    @Override
    public BoardRequestPageDTO getBoardRequestsByMember(Long memberId, Pageable pageable) {
        Page<BoardRequest> boardReqPage = boardReqRepo.findByRequesterIdOrderByUpdatedAtDesc(memberId, pageable);
        return BoardRequestPageDTO.of(boardReqPage);
    }

    @Override
    public Long createBoard(BoardCreateRequest createReq, Long adminId) {
        // 게시판 생성
        Board board = Board.create(createReq, adminId);
        Board newBoard = boardRepo.save(board);
        if (newBoard.getId() == null) {
            throw new CustomException(ErrorCode.BOARD_CREATE_FAILED);
        }
        return newBoard.getId();
    }

    @Override
    public void deleteBoard(Long boardId) {
        // 게시판 조회
        Board board = boardRepo.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시판을 찾을 수 없습니다."));

        // 게시판의 deletedYN 컬럼 값을 true로 변경
        board.setDeletedYN(true);

        // 수정된 게시판 저장
        boardRepo.save(board);
    }

    @Override
    public void createBoardRequest(BoardCreateRequest createReq, Long memberId) {
        // 게시판 중복 여부 확인
        if (boardRepo.existsByBoardNameAndDeletedYN(createReq.getBoardName(), false)) {
            throw new CustomException(ErrorCode.DUPLICATE_BOARDNAME);
        }

        // 게시판 요청 생성
        Member requester = Member.builder().id(memberId).build();
        BoardRequest boardReq = BoardRequest.create(createReq, requester);
        BoardRequest newBoardReq = boardReqRepo.save(boardReq);
        if (newBoardReq.getId() == null) {
            throw new CustomException(ErrorCode.BOARD_CREATE_FAILED);
        }
    }

    @Override
    public void cancelBoardRequest(Long boardReqId, Long memberId) {
        // 게시판 요청 내역 조회
        BoardRequest boardReq = boardReqRepo.findById(boardReqId)
                .orElseThrow(() -> new EntityNotFoundException("게시판 생성 요청 내역을 찾을 수 없습니다."));

        // 요청 회원 id 확인
        if (!Objects.equals(memberId, boardReq.getRequester().getId())) {
            throw new CustomException(ErrorCode.MISMATCH_REQUESTER_ID);
        }

        // 게시판 요청 내역 수정
        boardReq.setCanceledYN(true);

        // 수정된 게시판 요청 내역 저장
        boardReqRepo.save(boardReq);
    }

    @Override
    @Transactional
    public void updateBoardRequestStatus(Long boardReqId, Long adminId, int reqSts) {
        // 게시판 요청 내역 조회
        BoardRequest boardReq = boardReqRepo.findById(boardReqId)
                .orElseThrow(() -> new EntityNotFoundException("게시판 생성 요청 내역을 찾을 수 없습니다."));

        // 요청을 승인하는 경우 - 게시판 생성
        if (reqSts == 1) {
            BoardCreateRequest createReq = BoardCreateRequest.from(boardReq.getBoardName(), boardReq.getDescription());
            Long boardId = createBoard(createReq, adminId);
            boardReq.setBoardId(boardId);
        }

        // 게시판 요청 상태 수정
        Member approver = Member.builder().id(adminId).build();
        boardReq.setApprover(approver);
        boardReq.setReqSts(reqSts);

        // 수정된 게시판 요청 내역 저장
        boardReqRepo.save(boardReq);
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
