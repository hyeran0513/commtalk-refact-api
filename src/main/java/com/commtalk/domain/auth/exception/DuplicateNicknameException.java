package com.commtalk.domain.auth.exception;

public class DuplicateNicknameException extends RuntimeException {

    public DuplicateNicknameException() {
        super();
    }

    public DuplicateNicknameException(String message) {
        super(message);
    }

}
