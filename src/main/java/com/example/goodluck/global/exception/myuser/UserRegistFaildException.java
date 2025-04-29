package com.example.goodluck.global.exception.myuser;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.service.user.in.UserRegisterParam;


public class UserRegistFaildException extends RuntimeException {
    UserRegisterParam registUserRequestDto;

    public UserRegistFaildException(String message) {
        super(message);
    }
    public UserRegistFaildException(String message, MyUser myUser){
        super(message);
        registUserRequestDto = new UserRegisterParam();
        registUserRequestDto.setUserId(myUser.getUserId());
        registUserRequestDto.setUserName(myUser.getUserName());
        registUserRequestDto.setUserPw(myUser.getUserPw());
        registUserRequestDto.setUserEmail(myUser.getUserEmail());
        registUserRequestDto.setPostNo(myUser.getPostNo());
        registUserRequestDto.setAddressMain(myUser.getAddressMain());
        registUserRequestDto.setAddressDetail(myUser.getAddressDetail());
        registUserRequestDto.setTelNo(myUser.getTelNo());
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
    public UserRegisterParam getRegistUserRequestDto() {
        return registUserRequestDto;
    }
    
}
