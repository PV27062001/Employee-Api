package com.employeeApplication.exception;

import javax.naming.NameAlreadyBoundException;

public class UserNameAuthenticationException extends NameAlreadyBoundException {
    public UserNameAuthenticationException(String message){
        super(message);
    }
}
