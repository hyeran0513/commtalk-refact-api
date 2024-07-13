package com.commtalk.domain.member.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        super();
    }

    public PasswordMismatchException(String message) {
        super(message);
    }

}
