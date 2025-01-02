package com.example.goodluck.domain;

import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class MyUser {

    private Long userNo;
    private String userName;
    private String userEmail;
    private MultipartFile profileFile;
    @Getter(AccessLevel.NONE)
    private byte[] userProfile;
    private String userId;
    private String userPw;
    private String postNo;
    private String addressMain;
    private String addressDetail;
    private String telNo;

    public byte[] getUserProfile() {
        try{
            if(profileFile != null) {
                return profileFile.getBytes();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return userProfile;
    }
}
