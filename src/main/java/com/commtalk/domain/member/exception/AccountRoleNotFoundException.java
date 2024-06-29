package com.commtalk.domain.member.exception;

public class AccountRoleNotFoundException extends RuntimeException {

    public AccountRoleNotFoundException() {
        super();
    }

    public AccountRoleNotFoundException(String message) {
        super(message);
    }

}
