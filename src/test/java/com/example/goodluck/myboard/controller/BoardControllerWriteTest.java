package com.example.goodluck.myboard.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.goodluck.myboard.BoardContorller;
import com.example.goodluck.myboard.BoardService;
import com.example.goodluck.myuser.UserService;

@WebMvcTest(BoardContorller.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("BoardController의 글작성 테스트")
public class BoardControllerWriteTest {
    @MockBean
    private BoardService boardService;
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;
    
    @Nested
    class SuccessCase{
        @Test
        @DisplayName("글작성에 성공")
        void successWriteBoard(){
            // given
            // when
            // then
        }
    }
}
