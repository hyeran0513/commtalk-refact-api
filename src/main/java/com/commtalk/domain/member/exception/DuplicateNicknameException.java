package com.commtalk.domain.member.exception;

public class DuplicateNicknameException extends RuntimeException {

    public DuplicateNicknameException() {
        super();
    }

    public DuplicateNicknameException(String message) {
        super(message);
    }

}
