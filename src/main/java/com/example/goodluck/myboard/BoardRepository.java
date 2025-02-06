package com.example.goodluck.myboard;

import java.util.List;
import java.util.Optional;

import com.example.goodluck.domain.MyBoard;

public interface BoardRepository {
    Optional<MyBoard> selectBoard(Long boardNo);
    List<MyBoard> selectBoardList(Long start, Long end);
    MyBoard insertNew(MyBoard newBoard);
    int updateBoard(MyBoard board);
    int deleteBoard(Long boardNo);
    int updateBoardViewCnt(Long boardNo, int viewCnt);
    Long selectBoardCnt();
}
