package com.example.goodluck.exception.myuser;

public class UserPwNotMatchedException extends RuntimeException{
    public UserPwNotMatchedException(String message) {
        super(message);
    }
}
