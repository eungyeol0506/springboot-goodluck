package com.example.goodluck.service.board;

import com.example.goodluck.global.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BoardError implements ErrorCode{
    BOARD_NOT_FOUND(404, "찾을 수 없는 게시글입니다."),
    NOT_AUTHENTICATED(403, "인증되지 않은 사용자입니다.")
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
