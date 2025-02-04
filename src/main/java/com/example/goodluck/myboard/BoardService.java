package com.example.goodluck.myboard;

import java.time.LocalDate;
import java.util.List;

import com.example.goodluck.domain.Myboard;

public class BoardService {
    private final int LIST_SIZE = 15;
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }
    
    public void writeBoard(Myboard newBoard){
        newBoard.setCreateDate(LocalDate.now());
        boardRepository.insertNew(newBoard);
    }
    
    public Myboard getBoardDetail(Long boardNo){
        boardRepository.updateBoardViewCnt(boardNo);
        return boardRepository.selectBoard(boardNo).orElseThrow(() -> new IllegalStateException());
    }
    public List<Myboard> getBoardList(int page){
        int start = ((page-1) * LIST_SIZE) + 1;
        int end = page * LIST_SIZE;
        return boardRepository.selectBoardList(start, end);
    }

    public void eidtBoard(Myboard board){
        board.setUpdateDate(LocalDate.now());
        boardRepository.updateBoard(board);
    }


}
