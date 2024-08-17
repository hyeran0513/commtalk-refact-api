package com.commtalk.domain.board.service;

import com.commtalk.domain.board.dto.BoardDTO;
import com.commtalk.domain.board.dto.BoardWithPinDTO;
import com.commtalk.domain.board.dto.PinnedBoardDTO;
import com.commtalk.domain.board.dto.request.BoardCreateRequest;

import java.util.List;

public interface BoardService {

    List<BoardDTO> getAllBoard();

    List<BoardWithPinDTO> getAllBoardWithPin(Long memberId);

    BoardDTO getBoard(Long boardId);

    void isExistsBoard(Long boardId);

    List<PinnedBoardDTO> getPinnedBoards(Long memberId);

    void createBoard(BoardCreateRequest createReq, Long adminId);

    void pinDefaultBoard(Long memberId);

    void pinAndUnpinBoards(Long memberId, List<BoardWithPinDTO> pinReqList);

    void pinBoards(Long memberId, List<Long> boardIds);

    void unpinBoards(Long memberId, List<Long> boardIds);

    void reorderPinnedBoards(Long memberId, List<Long> boardIdList);

}
