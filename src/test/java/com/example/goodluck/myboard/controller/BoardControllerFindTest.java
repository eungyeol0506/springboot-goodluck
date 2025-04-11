package com.example.goodluck.myboard.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.goodluck.domain.MyAttach;
import com.example.goodluck.domain.MyBoard;
import com.example.goodluck.exception.myboard.ForbiddenBoardAccessException;
import com.example.goodluck.myboard.AttachService;
import com.example.goodluck.myboard.BoardContorller;
import com.example.goodluck.myboard.BoardService;
import com.example.goodluck.myuser.UserService;

@WebMvcTest(BoardContorller.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("BoardController의 조회 테스트")
public class BoardControllerFindTest {

    @MockBean
    private BoardService mockBoardService;
    @MockBean
    private UserService mockUserService;
    @MockBean
    private AttachService mockAttachService;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("")
    class GetBoardDetail{
        @Test
        @DisplayName("데이터 조회에 성공한 경우")
        void successGetDetail() throws Exception{
            // given
            MyBoard testBoard = MyBoard.createDummy(0L, "test title", "test Contents", 1L);
            List<MyAttach> testAttaches = new ArrayList<>();

            BDDMockito.given(mockBoardService.getBoardDetail(anyLong())).willReturn(testBoard);
            BDDMockito.given(mockAttachService.getAttachList(anyLong())).willReturn(testAttaches);

            // when then
            mockMvc.perform(MockMvcRequestBuilders.get("/board/123"))
                   .andExpect(status().isOk())
                   .andExpect(view().name("myboard/board"))
                   .andExpect(model().attribute("board", testBoard));
                   
        }

        @Test
        @DisplayName("게시글 상세 조회에 실패한 경우 (값이 없음)")
        void failGetCauseForbiddenBoardException() throws Exception{
            // given
            BDDMockito.given(mockBoardService.getBoardDetail(anyLong())).willThrow(new ForbiddenBoardAccessException("게시글이 없습니다.", null));

            // when then
            mockMvc.perform(MockMvcRequestBuilders.get("/board/123"))
                   .andExpect(status().is3xxRedirection())
                   .andExpect(redirectedUrl("/list"))
                    ;
            // "redirect:/list"
        }

    }
    
}
