package com.example.goodluck.global;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServiceExcepction extends RuntimeException{
    
    private final ErrorCode errorCode;
    
    public String getErrorMessage(){
        return errorCode.getMessage();
    }
    
    public ErrorCode getErrorCode(){
        return errorCode;
    }
}
