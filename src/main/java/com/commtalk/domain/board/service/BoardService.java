package com.commtalk.domain.board.service;

import com.commtalk.domain.board.dto.BoardDTO;
import com.commtalk.domain.board.dto.PinnedBoardDTO;

import java.util.List;

public interface BoardService {

    List<BoardDTO> getAllBoard();

    BoardDTO getBoard(Long boardId);

    List<PinnedBoardDTO> getPinnedBoards(Long memberId);

    void pinDefaultBoardByMember(Long memberId);

}
