package com.example.goodluck.myboard;

import java.util.List;
import java.util.Optional;

import com.example.goodluck.domain.Myboard;

public interface BoardRepository {
    Optional<Myboard> selectBoard(Long boardNo);
    List<Myboard> selectBoardList(int start, int end);
    Myboard insertNew(Myboard newBoard);
    int updateBoard(Myboard board);
    int deleteBoard(Long boardNo);
    int updateBoardViewCnt(Long boardNo);
}
