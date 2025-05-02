package com.example.goodluck.service.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.goodluck.domain.MyComment;

@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired CommentService commentService;

    @Test
    @DisplayName("댓글 작성을 성공함")
    void successSave(){
        // given
        MyComment comment = MyComment.builder()
                                .boardNo(9999L)
                                .reply("test")
                                .userNo(1L)
                                .build();
        // when
        commentService.save(comment);

        // then
        List<MyComment> comments = commentService.findByBoardNo(9999L);
        assertFalse(comments.isEmpty());
        assertEquals("test", comments.get(0).getReply());

    }

    @Test
    @DisplayName("댓글을 삭제에 성공함")
    void successRemove(){
        Long boardNo = 9999L;
        MyComment comment = MyComment.builder()
                                .boardNo(boardNo)
                                .reply("test")
                                .userNo(1L)
                                .build();
        commentService.save(comment);

        // when
        commentService.removeByBoardNo(boardNo);

        // then
        List<MyComment> comments = commentService.findByBoardNo(9999L);
        assertTrue(comments.isEmpty());
    }
}
