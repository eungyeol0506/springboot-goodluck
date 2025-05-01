package com.example.goodluck.service.user;

import com.example.goodluck.global.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserError implements ErrorCode{
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    USER_ID_DUPLICATED(400, "중복된 아이디입니다."),
    USER_PW_NOT_MATCHED(400,"비밀번호가 올바르지 않습니다."),
    IS_NOT_USER_PW(400, "기존 비밀번호 값이 올바르지 않습니다."),
    IS_SAME_PW(400, "기존과 동일한 비밀번호는 설정할 수 없습니다."),
    USER_REGIST_FAILED(500, "회원정보 생성에 실패하였습니다."),
    USER_UPDATE_FAILED(500, "회원정보 변경에 실패하였습니다."),
    CHANGE_PW_FAILED(500, "비밀번호 변경에 실패하였습니다.")
    ;

    private final int code;
    private final String message;
    
    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
    
}
