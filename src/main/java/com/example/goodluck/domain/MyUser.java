package com.example.goodluck.domain;

import org.springframework.web.multipart.MultipartFile;

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

}
