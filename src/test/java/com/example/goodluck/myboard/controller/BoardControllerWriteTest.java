package com.example.goodluck.myboard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.goodluck.domain.MyBoard;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.myboard.BoardContorller;
import com.example.goodluck.myboard.BoardService;
import com.example.goodluck.myuser.UserService;

@WebMvcTest(BoardContorller.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("BoardController의 글작성 테스트")
public class BoardControllerWriteTest {
    @MockBean
    private BoardService mockBoardService;
    @MockBean
    private UserService mockUserService;

    @Autowired
    private MockMvc mockMvc;
    
    
    @Nested
    class SuccessCase{
        @Test
        @DisplayName("글작성에 성공")
        void successWriteBoard() throws Exception{
            // given
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("userNo", 1L);
            
            BDDMockito.given(mockUserService.getUserInfo(1L)).willReturn(Optional.of(MyUser.creatDummy(1L)));
            BDDMockito.given(mockBoardService.writeBoard(any(MyBoard.class))).willReturn(MyBoard.createDummy(1L, "Test", "Test", 1L));
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap();
            formData.add("boardTitle", "Test");
            formData.add("contents", "Test");
            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post("/board/write")
                                                  .session(session)
                                                  .params(formData)
                                                  .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/board/1"))
                            .andDo(print());
            Mockito.verify(mockUserService).getUserInfo(1L);
            Mockito.verify(mockBoardService).writeBoard(any(MyBoard.class));
        }
    }

    @Nested
    class FailCase{
        @Test
        @DisplayName("필수값이 입력되지 않은 경우")
        void failInvalidInputData() throws Exception{
            // given
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("userNo", 1L);
            
            BDDMockito.given(mockUserService.getUserInfo(1L)).willReturn(Optional.of(MyUser.creatDummy(1L)));
            BDDMockito.given(mockBoardService.writeBoard(any(MyBoard.class))).willReturn(MyBoard.createDummy(1L, "Test", "Test", 1L));
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap();
            formData.add("boardTitle", null);
            formData.add("contents", "Test");
            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post("/board/write")
                                                  .session(session)
                                                  .params(formData)
                                                  .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                            .andExpect(status().isOk())
                            .andExpect(view().name("myboard/newboard_form"))
                            .andExpect(model().attributeExists("notice"))
                            .andExpect(model().attributeExists("preValue"));
            
            Mockito.verifyNoInteractions(mockUserService);                
            Mockito.verifyNoInteractions(mockBoardService);
        }

        @Test
        @DisplayName("유저 정보를 조회하지 못한 경우")
        void failInvalidUser() throws Exception{
            // given
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("userNo", 1L);
            
            BDDMockito.given(mockUserService.getUserInfo(anyLong())).willReturn(Optional.empty());
            BDDMockito.given(mockBoardService.writeBoard(any(MyBoard.class))).willReturn(MyBoard.createDummy(1L, "Test", "Test", 1L));
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap();
            formData.add("boardTitle", "Test");
            formData.add("contents", "Test");
            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post("/board/write")
                                                  .session(session)
                                                  .params(formData)
                                                  .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                            .andExpect(status().isOk())
                            // GlobalExceptionHandler가 처리
                            .andExpect(view().name("home"))
                            .andExpect(model().attributeExists("notice"));

            Mockito.verify(mockUserService).getUserInfo(anyLong());
            Mockito.verifyNoInteractions(mockBoardService);
        }
        
        @Test
        @DisplayName("세션정보가 null 인 경우")
        void failInvalidSession() throws Exception{
            // given
            MockHttpSession session = new MockHttpSession();

            BDDMockito.given(mockUserService.getUserInfo(1L)).willReturn(Optional.empty());
            BDDMockito.given(mockBoardService.writeBoard(any(MyBoard.class))).willReturn(MyBoard.createDummy(1L, "Test", "Test", 1L));
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap();
            formData.add("boardTitle", "Test");
            formData.add("contents", "Test");
            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post("/board/write")
                                                  .session(session)
                                                  .params(formData)
                                                  .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                            .andExpect(status().isOk())
                            // GlobalExceptionHandler가 처리
                            .andExpect(view().name("home"))
                            .andExpect(model().attributeExists("notice"));

            Mockito.verifyNoInteractions(mockUserService);
            Mockito.verifyNoInteractions(mockBoardService);
        }

        @Test
        @DisplayName("이외의 예외가 발생한 경우")
        void failFromElseException() throws Exception{
            // given
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("userNo", 1L);
            
            // userService에서 exception 발생
            // BDDMockito.given(mockUserService.getUserInfo(anyLong())).willThrow(new RuntimeException("새로운 예외가 발생"));
            
            // boardService에서 Exception 발생
            BDDMockito.given(mockUserService.getUserInfo(anyLong())).willReturn(Optional.of(MyUser.creatDummy(1L)));
            BDDMockito.given(mockBoardService.writeBoard(any(MyBoard.class))).willThrow(new RuntimeException("새로운 예외가 발생"));
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap();
            formData.add("boardTitle", "Test");
            formData.add("contents", "Test");
            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post("/board/write")
                                                .session(session)
                                                .params(formData)
                                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"));
                    // GlobalExceptionHandler가 처리

            // userService에서 exception 발생
            // Mockito.verify(mockUserService).getUserInfo(anyLong());
            // Mockito.verifyNoInteractions(mockBoardService);
            
            // boardService에서 Exception 발생
            Mockito.verify(mockUserService).getUserInfo(anyLong());
            Mockito.verify(mockBoardService).writeBoard(any(MyBoard.class));
        }
    }
}
