package com.example.goodluck.domain;

import java.beans.Transient;

import org.springframework.data.annotation.AccessType;

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

    
    public String getProfile(){
        if (profileImgPath != null){
            return "/files/" + profileImgName;
        }
        return null;
    }
}
