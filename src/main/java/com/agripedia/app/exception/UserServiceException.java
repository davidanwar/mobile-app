package com.agripedia.app.exception;

public class UserServiceException extends RuntimeException {

    private static final long serialVersionUID = -2136767588005414554L;
    public UserServiceException(String message) {
        super(message);
    }
}
