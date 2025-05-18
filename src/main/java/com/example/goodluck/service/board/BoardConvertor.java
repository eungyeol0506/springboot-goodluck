package com.example.goodluck.service.board;

import org.springframework.stereotype.Service;

import com.example.goodluck.domain.MyBoard;
import com.example.goodluck.service.board.dto.BoardModifyRequest;
import com.example.goodluck.service.board.dto.BoardWriteRequest;

@Service
public class BoardConvertor {

    public MyBoard toDomain(BoardWriteRequest param){
        return MyBoard.builder()
                .boardTitle(param.getBoardTitle())
                .contents(param.getContents())
                .build();
    }
    
    public MyBoard toDomain(BoardModifyRequest param){
        return MyBoard.builder()
                    .boardNo(param.getBoardNo())
                    .boardTitle(param.getBoardTitle())
                    .contents(param.getContents())
                    .build();
    }

}
