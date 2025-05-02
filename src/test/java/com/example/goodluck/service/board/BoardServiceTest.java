package com.example.goodluck.service.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.goodluck.domain.MyBoard;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.global.ServiceExcepction;
import com.example.goodluck.service.board.dto.BoardWriteRequest;
import com.example.goodluck.service.user.UserService;

@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired BoardService boardService;
    @Autowired UserService userService;

    @Test
    @DisplayName("게시글 작성 성공")
    void successWrite(){
        // given
        MyUser user = userService.getUser(201L);
        BoardWriteRequest param = new BoardWriteRequest();
        param.setBoardTitle("테스트");
        param.setContents("test");

        // when
        Long boardNo = boardService.write(user, param, null);

        // then
        MyBoard result = boardService.findBoardByNo(boardNo);
        assertEquals("테스트", result.getBoardTitle());
        assertEquals(user.getUserId(), result.getWriter().getUserId());

    }

    @Test
    @DisplayName("게시글 목록 조회 성공")
    void successFindAll(){
        Long pageNo = 1L;

        List<MyBoard> boards = boardService.findAll(pageNo);

        assertNotNull(boards);
        assertEquals(15, boards.size());
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void successDelete(){
        // given
        MyUser user = userService.getUser(201L);
        BoardWriteRequest param = new BoardWriteRequest();
        param.setBoardTitle("테스트");
        param.setContents("test");

        Long boardNo = boardService.write(user, param, null);

        // when
        boardService.delete(boardNo);

        // then
        Exception e = Assertions.catchException(()->boardService.findBoardByNo(boardNo));
        Assertions.assertThat(e).isInstanceOf(ServiceExcepction.class);
    }

    @Test
    @DisplayName("게시글 수정 성공")    
    void successModify(){
        // given

    }

}
