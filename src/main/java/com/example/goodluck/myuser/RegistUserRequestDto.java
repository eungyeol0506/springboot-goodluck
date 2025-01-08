package com.example.goodluck.myuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// import lombok.AccessLevel;
import lombok.Data;
// import lombok.Getter;

@Data
public class RegistUserRequestDto {
    @NotBlank(message = "사용자 이름은 필수값입니다.")
    private String userName;
    @Email(message = "이메일 형식이 아닙니다.")
    private String userEmail;
    @NotBlank(message = "사용자 아이디는 필수값입니다.")
    private String userId;
    @NotBlank(message = "사용자 아이디는 필수값입니다.")
    @Size(min=5, max=16, message = "비밀번호는 6글자 이상, 16글자를 초과할 수 없습니다.")
    private String userPw;
    @Size(max=5, message="유효하지 않은 우편번호 형식입니다.")
    private String postNo;
    private String addressMain;
    private String addressDetail;
    private String telNo;

}
