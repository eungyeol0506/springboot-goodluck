package com.example.goodluck.myboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.goodluck.domain.MyComment;

@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired CommentService commentService;

    @Nested
    class SuccessCase{
        @Test
        @DisplayName("댓글 작성 테스트")
        void successSave(){
            // given
            MyComment comment = getTestComment();
            
            // when
            commentService.save(comment);
    
            //then
            MyComment result = commentService.findByBoardNo(comment.getBoardNo()).get(0);
            assertNotNull(result.getCommentNo(), "insert 시 comment No가 set 되었음");
            assertEquals(LocalDate.now().getDayOfMonth(), result.getCreateDate().getDayOfMonth(), "현재 날짜로 저장됨");
            assertEquals(comment.getReply(), result.getReply(), "댓글 내용이 잘 저장됨");
        }
    
        @Test
        @DisplayName("댓글 삭제 테스트")
        void successRemoveByBoardNo(){
            // given
            List<MyComment> comments = getTestComments();
            for(MyComment comment : comments){
                commentService.save(comment);
            }
            
            // when
            commentService.removeByBoardNo(9999L);
            
            // then
            List<MyComment> result = commentService.findByBoardNo(9999L);
            assertTrue(result.isEmpty(), "해당 게시글의 모든 댓글이 삭제되어야 함");
        }
    }

    @Nested
    class FailCase{
        @Test
        @DisplayName("댓글 조회 시 argument 예외 발생")
        void failedFindByIllegalArgumentException(){
            // given
            Long boardNo = 0L;
            Long userNo = null;

            // when
            Exception ex1 = Assertions.catchException(() -> commentService.findByBoardNo(boardNo));
            Exception ex2 = Assertions.catchException(() -> commentService.findByUserNo(userNo));

            // then
            Assertions.assertThat(ex1).isInstanceOf(IllegalArgumentException.class);
            Assertions.assertThat(ex2).isInstanceOf(IllegalArgumentException.class);

        }
        
        @Test
        @DisplayName("댓글 작성 시 argument 예외 발생")
        void failedSaveByIllegalArgumentException(){
            // given
            MyComment comment = getTestComment();
            comment.setBoardNo(null);
    
            // when
            Exception exception = Assertions.catchException(() -> commentService.save(comment));

            // then
            Assertions.assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    
        }

    }

    private MyComment getTestComment() {
        return MyComment.builder()
            .boardNo(9999L)
            .userNo(1L)
            .reply("Test")
            .build();
    }

    private List<MyComment> getTestComments() {
        List<MyComment> comments = new ArrayList<>();
        for(int i=0; i<3; i++){
            comments.add( MyComment.builder() 
                                .boardNo(9999L)
                                .userNo(1L)
                                .reply("Test")
                                .build());

        }

        return comments;
    }

}
