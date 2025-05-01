package com.example.goodluck.service.user;

import com.example.goodluck.global.ServiceExcepction;

public class UserServiceException extends ServiceExcepction{

    // private MyUser user;

    public UserServiceException(UserError errorCode){
        super(errorCode);
    }
    
}
