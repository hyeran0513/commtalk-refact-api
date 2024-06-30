package com.commtalk.domain.board.exception;

public class BoardNotFoundException extends RuntimeException {

    public BoardNotFoundException() {
        super();
    }

    public BoardNotFoundException(String message) {
        super(message);
    }

}
