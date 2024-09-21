package com.commtalk.domain.board.service;

import com.commtalk.domain.board.dto.*;
import com.commtalk.domain.board.dto.request.BoardCreateRequest;
import com.commtalk.domain.board.entity.BoardRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {

    List<BoardDTO> getAllBoard();

    List<BoardWithPinDTO> getAllBoardWithPin(Long memberId);

    BoardDTO getBoard(Long boardId);

    void isExistsBoard(Long boardId);

    List<PinnedBoardDTO> getPinnedBoards(Long memberId);

    BoardRequestPageDTO getAllBoardRequest(Pageable pageable);

    BoardRequestPageDTO getBoardRequestsByMember(Long memberId, Pageable pageable);

    Long createBoard(BoardCreateRequest createReq, Long adminId);

    void createBoardRequest(BoardCreateRequest createReq, Long memberId);

    void cancelBoardRequest(Long boardReqId, Long memberId);

    void updateBoardRequestStatus(Long boardReqId, Long adminId, int reqSts);

    void pinDefaultBoard(Long memberId);

    void pinAndUnpinBoards(Long memberId, List<BoardWithPinDTO> pinReqList);

    void pinBoards(Long memberId, List<Long> boardIds);

    void unpinBoards(Long memberId, List<Long> boardIds);

    void reorderPinnedBoards(Long memberId, List<Long> boardIdList);

}
