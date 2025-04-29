package com.example.goodluck.global.exception.myuser;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.service.user.in.UserEditParam;
import com.example.goodluck.service.user.in.UserRegisterParam;

public class UserProfileImageUploadException extends RuntimeException {
    private final MyUser user; 
    public UserProfileImageUploadException(String message, MyUser user){
        super(message);
        this.user = user;
        this.printStackTrace();
    }

    public UserEditParam getEditUserRequestDto(){
        UserEditParam returnDto = new UserEditParam();
        returnDto.setUserNo(user.getUserNo());
        returnDto.setUserId(user.getUserId());
        returnDto.setUserName(user.getUserName());
        returnDto.setUserPw(user.getUserPw());
        returnDto.setUserEmail(user.getUserEmail());
        returnDto.setAddressDetail(user.getAddressDetail());
        returnDto.setAddressMain(user.getAddressMain());
        returnDto.setPostNo(user.getPostNo());
        returnDto.setTelNo(user.getTelNo());
        returnDto.setProfileImgPath(user.getProfileImgPath());
        returnDto.setProfileImgName(user.getProfileImgName());

        return returnDto;
    }

    public UserRegisterParam getRegistUserReqeustDto(){
        UserRegisterParam returnDto = new UserRegisterParam();
        returnDto.setUserId(user.getUserId());
        returnDto.setUserName(user.getUserName());
        returnDto.setUserPw(user.getUserPw());
        returnDto.setUserEmail(user.getUserEmail());
        returnDto.setAddressDetail(user.getAddressDetail());
        returnDto.setAddressMain(user.getAddressMain());
        returnDto.setPostNo(user.getPostNo());
        returnDto.setTelNo(user.getTelNo());

        return returnDto;
    }
}