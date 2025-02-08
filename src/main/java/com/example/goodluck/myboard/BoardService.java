package com.example.goodluck.myboard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.goodluck.domain.MyBoard;

public class BoardService {
    // 한 페이지당 리스트 수
    private final int LIST_SIZE = 15;
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }
    
    public MyBoard writeBoard(MyBoard newBoard){
        newBoard.setCreateDate(LocalDate.now());
        return boardRepository.insertNew(newBoard);
    }
    
    public MyBoard getBoardDetail(Long boardNo){
        MyBoard myBoard = boardRepository.selectBoard(boardNo)
                                         .orElseThrow(() -> new IllegalStateException());
        myBoard.increaseViewCnt();
        boardRepository.updateBoardViewCnt(boardNo, myBoard.getViewCnt());
        return myBoard;
    }
    public List<MyBoard> getBoardList(Long page){
        long start = ((page-1) * LIST_SIZE) + 1;
        long end = page * LIST_SIZE;
        return boardRepository.selectBoardList(start, end);
    }

    public void eidtBoard(MyBoard board){
        board.setUpdateDate(LocalDate.now());
        int updateRow = boardRepository.updateBoard(board);
        if (updateRow <= 0){
            throw new IllegalStateException("수정에 실패하였습니다.");
        }
    }
    public void deleteBoard(Long boardNo){
        int deleteRow = boardRepository.deleteBoard(boardNo);
        if (deleteRow <= 0){
            throw new IllegalStateException("삭제에 실패하였습니다.");
        }
    }

    public List<Integer> getPageNumbers(){
        Long totalCnt = boardRepository.selectBoardCnt();

        List<Integer> pageList = new ArrayList<>();
        // int idx = 1;
        long cnt = totalCnt;
        for(int i=1; i<10; i++){
            if(cnt > LIST_SIZE){
                pageList.add(i);
                cnt -= LIST_SIZE;
            }
        }

        if(cnt > 0){
            pageList.add(totalCnt.intValue()/15);
        }

        return pageList; 
    }
}
