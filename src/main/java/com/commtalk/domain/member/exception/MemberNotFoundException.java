package com.commtalk.domain.member.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super();
    }

    public MemberNotFoundException(String message) {
        super(message);
    }

}
