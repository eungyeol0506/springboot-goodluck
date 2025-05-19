package com.example.goodluck.service.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserEditRequest {

    @NotBlank(message = "사용자 이름은 필수값입니다.")
    private String userName;
    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수값입니다.")
    private String userEmail;
    @Size(max=5, message="유효하지 않은 우편번호 형식입니다.")
    private String postNo;
    private String telNo;
    private String addressMain;
    private String addressDetail;
    
    private String imageFullPath;
}
