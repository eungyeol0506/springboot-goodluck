package com.example.goodluck.myboard.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.goodluck.domain.MyBoard;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.exception.myboard.ForbiddenBoardAccessException;
import com.example.goodluck.myboard.BoardContorller;
import com.example.goodluck.myboard.BoardService;
import com.example.goodluck.myuser.UserService;

@WebMvcTest(BoardContorller.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("BoardController의 글수정 테스트")
public class BoardControllerEditTest {
    @MockBean
    private BoardService mockBoardService;
    @MockBean
    private UserService mockUserService;

    @Autowired
    MockMvc mockMvc;

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase{
        @Test
        @DisplayName("글 수정에 성공하는 경우")
        void successEditBoard() throws Exception{
            // given
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("userNo", 1L);
            MyBoard board = MyBoard.createDummy(1L, "test", "test", 1L);
            
            BDDMockito.given(mockBoardService.getBoardDetail(anyLong())).willReturn(board);

            // when then
            mockMvc.perform(MockMvcRequestBuilders.post("/board/form")
                                                .session(session)
                                                .param("boardNo", "1")
                                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().isOk())
                    .andExpect(view().name("myboard/board_form"))
                    .andExpect(request().sessionAttribute("userNo", 1L))
                    .andExpect(model().attributeExists("preValue"))
                    ;
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailCase{
        @Test
        @DisplayName("유저 권한이 없는 경우")
        void failInvalidSession() throws Exception{
            // 세션정보가 없거나 게시글 작성자의 식별값이 아닌 경우
            // given
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("userNo", 15L);
            MyBoard board = MyBoard.createDummy(1L, "test", "test", 1L);
            BDDMockito.given(mockBoardService.getBoardDetail(anyLong())).willReturn(board);
            // when then
            mockMvc.perform(MockMvcRequestBuilders.post("/board/form")
                                                .session(session)
                                                .param("boardNo", "1")
                                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/board/1"))
                    .andExpect(request().sessionAttribute("userNo", 15L))
                    .andExpect(flash().attribute("notice", "수정 권한이 없는 사용자입니다."))
                    ;
        }
        @Test
        @DisplayName("게시글이 변경되거나 없어진 경우")
        void failInvalidBoard() throws Exception{
            // 세션정보가 없거나 게시글 작성자의 식별값이 아닌 경우
            // given
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("userNo", 15L);

            BDDMockito.given(mockBoardService.getBoardDetail(anyLong())).willThrow(new ForbiddenBoardAccessException("test", null));
            // when then
            mockMvc.perform(MockMvcRequestBuilders.post("/board/form")
                                                .session(session)
                                                .param("boardNo", "1")
                                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/list"))
                    .andExpect(request().sessionAttribute("userNo", 15L))
                    .andExpect(flash().attribute("notice", "test"))
                    ;
        }
        @Test
        @DisplayName("그 외의 Exception 발생하는 경우 -> error 페이지")
        void failFromElseException() throws Exception{
            // given
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("userNo", 15L);

            BDDMockito.given(mockBoardService.getBoardDetail(anyLong())).willThrow(new IllegalStateException("test"));
            // when then
            mockMvc.perform(MockMvcRequestBuilders.post("/board/form")
                                                .session(session)
                                                .param("boardNo", "1")
                                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    ;
        }

    }
}
