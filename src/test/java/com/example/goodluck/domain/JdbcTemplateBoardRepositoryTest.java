package com.example.goodluck.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JdbcTemplateBoardRepositoryTest {

    @Autowired JdbcTemplateBoardRepository boardRepository;

    @Test
    @DisplayName("정상적으로 작성한 경우")
    void successSave(){
        // given
        MyBoard board = getTestBoard();

        // when
        boardRepository.save(board);

        // then
        MyBoard result = boardRepository.findAll(0L,3L).get(1); // 9999L 제외해야해서 1번임
        assertNotNull(result.getBoardNo(), "값이 저장됨");
        assertEquals(board.getBoardTitle(), result.getBoardTitle(), "같은 보드 맞음, 최신순 정렬임");
    }

    @Test
    @DisplayName("정상적으로 수정한 경우, view cnt")
    void successUpdate(){
        // given
        MyBoard board = getTestBoard();
        boardRepository.save(board);

        MyBoard thatBoard = boardRepository.findAll(0L,2L).get(1);
        thatBoard.setBoardTitle("수정된 제목");
        thatBoard.setViewCnt(thatBoard.getViewCnt() + 1);

        // when
        boardRepository.update(thatBoard);

        // then
        MyBoard result = boardRepository.findByNo(thatBoard.getBoardNo()).get();
        assertEquals(thatBoard.getBoardNo(), result.getBoardNo(), "result가 수정한 게시글 맞음");
        assertEquals("수정된 제목", result.getBoardTitle(), "제목이 수정되었음");
        assertNotEquals(0, result.getViewCnt(), "조회수 올라감");
    }

    @Test
    @DisplayName("삭제에 성공한 경우")
    void successRemove(){
        // given
        MyBoard board = getTestBoard();
        boardRepository.save(board);

        MyBoard thatBoard = boardRepository.findAll(0L,2L).get(1);
        // when
        boardRepository.remove(thatBoard.getBoardNo());

        // then
        MyBoard result = boardRepository.findByNo(thatBoard.getBoardNo()).orElse(null);
        assertNull(result, "삭제되어서 결과가 null이어야 함");
    }
    
    private MyBoard getTestBoard() {
        return MyBoard.builder()
                .boardTitle("테스트 글")
                .contents("내용부분")
                .viewCnt(0)
                .createDate(LocalDate.now())
                .writer(MyUser.builder()
                            .userNo(1L)
                            .build())       
                .build();
    }
}
