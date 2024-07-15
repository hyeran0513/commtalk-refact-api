package com.commtalk.domain.board.service;

import com.commtalk.domain.board.dto.BoardDTO;
import com.commtalk.domain.board.dto.PinnedBoardDTO;
import com.commtalk.domain.board.dto.request.BoardPinRequest;

import java.util.List;

public interface BoardService {

    List<BoardDTO> getAllBoard();

    BoardDTO getBoard(Long boardId);

    void isExistsBoard(Long boardId);

    List<PinnedBoardDTO> getPinnedBoards(Long memberId);

    void pinDefaultBoard(Long memberId);

    void pinAndUnpinBoards(Long memberId, List<BoardPinRequest> pinReqList);

    void pinBoards(Long memberId, List<Long> boardIds);

    void unpinBoards(Long memberId, List<Long> boardIds);

    void reorderPinnedBoards(Long memberId, List<Long> boardIdList);

}
