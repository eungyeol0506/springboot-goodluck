package com.example.goodluck.service.board;

import com.example.goodluck.global.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BoardError implements ErrorCode{
    BOARD_NOT_FOUND(404, "게시글을 찾을 수 없습니다.")
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
