package com.example.goodluck.exception.myuser;

import org.springframework.validation.BindingResult;

public class UserRegistFaildException extends RuntimeException {
    BindingResult bindingResult;

    public UserRegistFaildException(String message) {
        super(message);
    }
    
    public UserRegistFaildException(BindingResult bindingResult){
        this.bindingResult = bindingResult;
    }
    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return super.getMessage();
    }
    
}
