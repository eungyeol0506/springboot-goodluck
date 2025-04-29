package com.example.goodluck.global.exception.myuser;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.myuser.dto.EditUserRequestDto;
import com.example.goodluck.myuser.dto.RegistUserRequestDto;

public class UserProfileImageUploadException extends RuntimeException {
    private final MyUser user; 
    public UserProfileImageUploadException(String message, MyUser user){
        super(message);
        this.user = user;
        this.printStackTrace();
    }

    public EditUserRequestDto getEditUserRequestDto(){
        EditUserRequestDto returnDto = new EditUserRequestDto();
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

    public RegistUserRequestDto getRegistUserReqeustDto(){
        RegistUserRequestDto returnDto = new RegistUserRequestDto();
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