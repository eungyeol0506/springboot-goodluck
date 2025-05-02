package com.example.goodluck.service.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginRequest {
    @NotBlank(message = "사용자 아이디는 필수값입니다.")
    private String id;
    @NotBlank(message = "사용자 비밀번호는 필수값입니다.")
    private String pw;
    
}
