package com.example.goodluck.myboard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.goodluck.domain.MyBoard;
import com.example.goodluck.exception.myboard.ForbiddenBoardAccessException;

public class BoardService {
    // 한 페이지당 리스트 수
    private final int LIST_SIZE = 15;
    private BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }
    // @Autowired
    // private AttachService attachService;
    
    public MyBoard writeBoard(MyBoard newBoard){
        // set create date
        newBoard.setCreateDate(LocalDate.now());
        return boardRepository.insertNew(newBoard);
    }
    
    public MyBoard getBoardDetail(Long boardNo){
        MyBoard myBoard = boardRepository.selectBoard(boardNo)
                                         .orElseThrow(
                                            () -> new ForbiddenBoardAccessException("게시글이 없습니다.", null)
                                          );
        // increase view count                                   
        myBoard.increaseViewCnt();
        boardRepository.updateBoardViewCnt(boardNo, myBoard.getViewCnt());

        // select attach list
        // myBoard.setAttachList(attachService.getAttachList(boardNo));
        
        return myBoard;
    }
    public List<MyBoard> getBoardList(Long page){
        long start = ((page-1) * LIST_SIZE) + 1;
        long end = page * LIST_SIZE;
        return boardRepository.selectBoardList(start, end);
    }

    public void eidtBoard(MyBoard board){
        // set update date
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
