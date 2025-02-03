package com.example.goodluck.domain;

import java.beans.Transient;

import lombok.Data;

@Data
public class MyUser {

    private Long userNo;
    private String userName;
    private String userEmail;
    private String userId;
    private String userPw;
    private String postNo;
    private String addressMain;
    private String addressDetail;
    private String telNo;
    private String profileImgPath;
    private String profileImgName;

    public MyUser(){}
    public MyUser(Long userNo, String userName, String userEmail, String userId, String userPw, String postNo,
            String addressMain, String addressDetail, String telNo, String profileImgPath, String profileImgName) {
        this.userNo = userNo;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userId = userId;
        this.userPw = userPw;
        this.postNo = postNo;
        this.addressMain = addressMain;
        this.addressDetail = addressDetail;
        this.telNo = telNo;
        this.profileImgPath = profileImgPath;
        this.profileImgName = profileImgName;
    }
    
    @Transient
    public String getProfile(){
        if (profileImgPath != null){
            return "/files/" + profileImgName;
        }
        return null;
    }
}
