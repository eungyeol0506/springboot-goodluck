package com.example.goodluck.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class JdbcTemplateCommentRepositoryTest {

    @Autowired JdbcTemplateCommentRepository commentRepository;

    @Test
    @DisplayName("조회 성공")
    void successFind(){
        // given
        // 게시글의 첫 댓글이라 할 때
        Long boardNo = 9999L; 
        Long userNo = 241L; 
        MyComment comment = MyComment.builder()
                                .boardNo(boardNo)
                                .userNo(userNo)
                                .createDate(LocalDate.now())
                                .reply("TEST")
                                .build();
                            
        commentRepository.save(comment);
        // when
        MyComment result = commentRepository.findByBoardNo(boardNo).get(0);
        MyComment result2 = commentRepository.findByUserNo(userNo).get(0);

        // then
        assertEquals(result.getCommentNo(), result2.getCommentNo(), "같은 값을 조회함");
    }

    @Test
    @DisplayName("삭제 성공")
    void successRemove(){
        // given
        Long boardNo = 9999L; // DB 테스트할 게시글 번호
        List<Long> users = new ArrayList<>();
        users.add(204L);
        users.add(222L);
        users.add(223L);

        for (Long userNo : users){
            MyComment comment = MyComment.builder()
                                    .boardNo(boardNo)
                                    .userNo(userNo)
                                    .createDate(LocalDate.now())
                                    .reply("TEST")
                                    .build();
            
            commentRepository.save(comment);
        }
                            
        List<MyComment> before = commentRepository.findByBoardNo(boardNo);
        // when
        List<Long> comments = new ArrayList<>();
        for(MyComment comment : before){
            comments.add(comment.getCommentNo());
        }
        commentRepository.remove(comments);
        List<MyComment> after = commentRepository.findByBoardNo(boardNo);
        // then
        assertEquals(users.size(), before.size(), "입력한 값만큼 조회함");
        assertTrue(after.isEmpty(), "값이 삭제되어 빈 리스트여야 함");
    }

}
