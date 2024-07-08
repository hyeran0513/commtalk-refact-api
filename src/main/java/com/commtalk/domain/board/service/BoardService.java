package com.commtalk.domain.board.service;

import com.commtalk.domain.board.dto.BoardDTO;
import com.commtalk.domain.board.dto.BoardSimpleDTO;
import com.commtalk.domain.board.dto.PinnedBoardDTO;

import java.util.List;

public interface BoardService {

    List<BoardDTO> getAllBoard();

    BoardDTO getBoard(Long boardId);

    void isExistsBoard(Long boardId);

    List<PinnedBoardDTO> getPinnedBoards(Long memberId);

    void pinDefaultBoard(Long memberId);

    void pinBoards(Long memberId, List<Long> boardIds);

    void unpinBoards(Long memberId, List<Long> boardIds);

    void reorderPinnedBoards(Long memberId, List<PinnedBoardDTO> boardDtoList);

}
