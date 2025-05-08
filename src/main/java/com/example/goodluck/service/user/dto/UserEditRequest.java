package com.example.goodluck.service.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserEditRequest {
    // form 에서 작성하는 내용 
    // 변수명은 form 내 id 태그값
    @NotNull(message = "사용자 번호는 필수값입니다.")
    private Long userNo;
    @NotBlank(message = "사용자 이름은 필수값입니다.")
    private String userName;
    @Email(message = "이메일 형식이 아닙니다.")
    private String userEmail;
    @Size(max=5, message="유효하지 않은 우편번호 형식입니다.")
    private String postNo;
    private String telNo;
    private String addressMain;
    private String addressDetail;
    private String profileImgPath;
    private String profileImgName;
}
