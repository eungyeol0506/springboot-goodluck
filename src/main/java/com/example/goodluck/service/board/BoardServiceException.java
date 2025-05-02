package com.example.goodluck.service.board;

import com.example.goodluck.global.ServiceExcepction;

public class BoardServiceException extends ServiceExcepction{

    public BoardServiceException(BoardError errorCode) {
        super(errorCode);
    }
    
    
}
