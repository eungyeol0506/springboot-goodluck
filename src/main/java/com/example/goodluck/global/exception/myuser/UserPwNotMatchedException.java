package com.example.goodluck.global.exception.myuser;

public class UserPwNotMatchedException extends RuntimeException{
    public UserPwNotMatchedException(String message) {
        super(message);
    }
}
