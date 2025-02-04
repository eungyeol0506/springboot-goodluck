package com.example.goodluck.myboard;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.domain.Myboard;

@SpringBootTest
@Transactional
public class JdbcTemplateBoardRepositoryTest {

    private final JdbcTemplateBoardRepository boardRepository;

    @Autowired
    public JdbcTemplateBoardRepositoryTest(DataSource dataSource) {
        this.boardRepository = new JdbcTemplateBoardRepository(dataSource);
    }

    @Nested
    class testInsertQuery{

        @DisplayName("값이 정상적으로 들어가는 경우")
        @Test
        void successCase(){
            // given 
            MyUser user = new MyUser();
            user.setUserNo(1L);
            Myboard board = new Myboard(0L, "TEST 20250205", "테스트코드에서 작성한 글", 0, LocalDate.now(), LocalDate.now(), user);
            // when
            Myboard result = boardRepository.insertNew(board);
            // then
            Assertions.assertThat(board).isEqualTo(result);
        }

    }
    @Nested
    class testSelectQuery{
        @Test
        @DisplayName("성공하는 경우")
        void successCase(){
            // given 
            Long selectNo = 5L;
            int start = 1; int end = 5;
            int listSize = end-(start-1);

            // when
            Myboard result1 = boardRepository.selectBoard(selectNo).get();
            List<Myboard> result2 = boardRepository.selectBoardList(start, end);

            // then
            Assertions.assertThat(result1.getBoardNo()).isEqualTo(selectNo);
            Assertions.assertThat(result2.size()).isEqualTo(listSize);
        }
    }

    @Nested
    class testUpdateQuery{
        @DisplayName("게시글 수정에 성공한 경우 title, contents, update date가 업뎃가능")
        @Test
        void successCase(){
            // given
            String title = "new Title";
            String contents = "new contents";

            Myboard board = boardRepository.selectBoard(5L).get();
            board.setBoardTitle(title);
            board.setContents(contents);
            board.setUpdateDate(LocalDate.now());
            //when
            boardRepository.updateBoard(board);
            Myboard result = boardRepository.selectBoard(5L).get();
            //then
            Assertions.assertThat(result.getBoardTitle()).isEqualTo(title);
            Assertions.assertThat(result.getContents()).isEqualTo(contents);
            Assertions.assertThat(result.getUpdateDate()).isEqualTo(LocalDate.now());
        }

        @DisplayName("조회수 업데이트에 성공하는 경우")
        @Test
        void successCase2(){
            // given
            Long boardNo = 1L;
            Myboard board = boardRepository.selectBoard(boardNo).get();
            // when
            boardRepository.updateBoardViewCnt(boardNo);
            Myboard result = boardRepository.selectBoard(boardNo).get();
            // then
            Assertions.assertThat(result.getViewCnt()).isNotEqualTo(board.getViewCnt());
        }
    }

    @Nested
    class testDeleteQuery{
        @Test
        @DisplayName("게시글 삭제에 성공한 경우")
        void successCase(){
            // given
            // Myboard board = boardRepository.selectBoard(2L).get();
            // when
            boardRepository.deleteBoard(2L);
            Myboard board = boardRepository.selectBoard(2L).orElse(null);
            // then
            Assertions.assertThat(board).isEqualTo(null);
        }
    }
    
}
