package com.example.goodluck.exception.myboard;

import com.example.goodluck.domain.MyBoard;

public class ForbiddenBoardAccessException extends RuntimeException {
    private MyBoard boardInfo;
    public ForbiddenBoardAccessException(String message, MyBoard board) {
        super(message);
        this.boardInfo = board;
    }
    public MyBoard getBoardInfo(){
        return this.boardInfo;
    }
    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return super.getMessage();
    }

    @Override
    public void printStackTrace() {
        // TODO Auto-generated method stub
        super.printStackTrace();
    }
    
}
