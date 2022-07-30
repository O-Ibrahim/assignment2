package com.pwc.assignment.exception;

public class UsernameAlreadyUsedException extends Exception{
    public UsernameAlreadyUsedException() {
        super();
    }

    public UsernameAlreadyUsedException(String message) {
        super(message);
    }

    public UsernameAlreadyUsedException(String message, Throwable cause) {
        super(message, cause);
    }
}
