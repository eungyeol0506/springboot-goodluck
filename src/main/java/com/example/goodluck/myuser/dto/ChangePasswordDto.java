package com.example.goodluck.myuser.dto;

import java.util.HashMap;
import java.util.Map;

import com.example.goodluck.common.MyDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDto implements MyDto {
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
    
    @Override
    public Map<String,String> toDomain() {
        Map<String,String> result = new HashMap<>();

        result.put("newPw", newPw);
        result.put("oldPw", oldPw);

        return result;
    }

}
