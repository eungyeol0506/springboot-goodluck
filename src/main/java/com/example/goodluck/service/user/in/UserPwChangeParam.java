package com.example.goodluck.service.user.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserPwChangeParam {
    @NotBlank(message = "이전 비밀번호 값을 입력해주세요.")
    private String oldPw;

    @NotBlank(message = "새 비밀번호 값을 입력해주세요.")
    private String newPw;
    
    @NotBlank(message = "새 비밀번호 확인 값을 입력해주세요.")
    private String newPwCfm;

    public boolean isNotConfirmPasswordValue(){
        if(this.newPw.equals(newPwCfm)){
            return true;
        }
        return false;
    }

}
