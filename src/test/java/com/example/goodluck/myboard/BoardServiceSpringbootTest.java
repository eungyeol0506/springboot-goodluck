package com.example.goodluck.myboard;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.goodluck.domain.MyBoard;

@SpringBootTest
public class BoardServiceSpringbootTest {

    @Autowired BoardService boardService;

    @Test
    @DisplayName("게시글 목록 조회")
    void successGetBoardList(){
        // given
        long page = 1L;

        // when
        List<MyBoard> boardList = boardService.getBoardList(page);

        // then
        assertNotNull(boardList, "반환값이 널이 아님");
    }

    @Test
    @DisplayName("전체 게시글을 보여줄 페이지 번호 조회")
    void successGetBoardPages(){

        List<Integer> pageList = boardService.getPageNumbers();

        // then
        assertNotNull(pageList, "반환값이 널이 아님");
    }
}
