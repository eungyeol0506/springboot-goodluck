package com.example.goodluck.service.user;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.global.ServiceExcepction;

public class UserServiceException extends ServiceExcepction{

    private MyUser user;

    public UserServiceException(UserError errorCode){
        super(errorCode);
    }
    public UserServiceException(UserError errorCode, MyUser user){
        super(errorCode);
        this.user = user;
    }

    public MyUser getUser(){
        return this.user;
    }
}
