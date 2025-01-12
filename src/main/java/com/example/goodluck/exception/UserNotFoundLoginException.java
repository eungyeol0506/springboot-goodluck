package com.example.goodluck.exception;

public class UserNotFoundLoginException extends RuntimeException {
    public UserNotFoundLoginException(String message) {
        super(message);
    }
}
