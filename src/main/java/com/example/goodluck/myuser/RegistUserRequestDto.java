package com.example.goodluck.myuser;

import jakarta.validation.constraints.NotBlank;
// import lombok.AccessLevel;
import lombok.Data;
// import lombok.Getter;

@Data
public class RegistUserRequestDto {
    @NotBlank
    private String userName;
    @NotBlank
    private String userEmail;
    @NotBlank
    private String userId;
    @NotBlank
    private String userPw;
    
    private String postNo;
    private String addressMain;
    private String addressDetail;
    private String telNo;

    // public static RegistUserRequestDto newInstance( String userName,
    //                                                 String userEmail,
    //                                                 String userId,
    //                                                 String userPw 
    // ){
    //     RegistUserRequestDto registUserRequestDto = new RegistUserRequestDto();
    //     registUserRequestDto.userName = userName;
    //     registUserRequestDto.userEmail = userEmail;
    //     registUserRequestDto.userId = userId;
    //     registUserRequestDto.userPw = userPw;
    //     return registUserRequestDto;
    // }
}
