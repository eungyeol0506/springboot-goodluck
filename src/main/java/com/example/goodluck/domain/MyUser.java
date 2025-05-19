package com.example.goodluck.domain;

import java.util.Optional;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyUser {
    // pk
    private Long userNo;

    private String userId;
    private String userPw;
    
    private String userEmail;
    private String userName;
    private String postNo;
    private String addressMain;
    private String addressDetail;
    private String telNo;
    // image
    private String profileImgPath;
    private String profileImgName;
    
    @RequiredArgsConstructor
    public enum UserConstants{
        PRIVATE_KEY("USER_NO"),
        TABLE_NAME("MY_USER"),
        SEQUENCE_NAME("MY_USER_SEQ");

        private final String value;
        public String getValue(){
            return value;
        }
    }

    public String getImageResource(){
        if (Optional.ofNullable(profileImgName).isEmpty()){
            return null;
        }
        
        return "/uploads/profiles/" + profileImgName;
    }
}
