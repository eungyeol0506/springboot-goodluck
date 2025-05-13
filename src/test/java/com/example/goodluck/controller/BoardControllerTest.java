package com.example.goodluck.controller;

import static org.mockito.ArgumentMatchers.anyLong;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.example.goodluck.domain.MyBoard;
import com.example.goodluck.service.board.BoardService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @Nested
    @DisplayName("게시글 조회")
    class Find{
        
        @Nested
        class Success{
            @Test
            @DisplayName("게시글 목록 조회 성공")
            void findAll() throws Exception {
                // given
                List<MyBoard> boards = Arrays.asList(
                    MyBoard.builder()
                        .boardNo(1L)
                        .boardTitle("테스트 제목1")
                        .contents("테스트 내용1")
                        .build(),
                    MyBoard.builder()
                        .boardNo(2L)
                        .boardTitle("테스트 제목2") 
                        .contents("테스트 내용2")
                        .build()
                );
                
                List<Integer> pages = Arrays.asList(1, 2, 3);
                
                // Debug: Print mock setup
                System.out.println("Setting up mocks with boards: " + boards);
                System.out.println("Setting up mocks with pages: " + pages);
                
                BDDMockito.given(boardService.findAll(anyLong())).willReturn(boards);
                BDDMockito.given(boardService.getPageNumbers()).willReturn(pages);

                // when & then
                MvcResult result = mockMvc.perform(get("/list/{page}", 1L))
                    .andDo(mvcResult -> {
                        // Debug: Print actual response details
                        System.out.println("Response Status: " + mvcResult.getResponse().getStatus());
                        System.out.println("View Name: " + mvcResult.getModelAndView().getViewName());
                        System.out.println("Model: " + mvcResult.getModelAndView().getModel());
                        if (mvcResult.getResolvedException() != null) {
                            System.out.println("Exception: " + mvcResult.getResolvedException());
                        }
                    })
                    .andExpect(status().isOk())
                    .andExpect(view().name("board/list"))
                    .andExpect(model().attributeExists("boards"))
                    .andReturn();

                // Debug: Additional verification
                System.out.println("Final view name: " + result.getModelAndView().getViewName());
                System.out.println("Final model: " + result.getModelAndView().getModel());
            }

            @Test
            @DisplayName("게시글 상세 조회 성공")
            void findById() throws Exception {
                // given
                MyBoard board = MyBoard.builder()
                    .boardNo(1L)
                    .boardTitle("테스트 제목")
                    .contents("테스트 내용")
                    .build();
                
                // Debug: Print mock setup
                System.out.println("Setting up mock with board: " + board);
                
                BDDMockito.given(boardService.findBoardByNo(anyLong())).willReturn(board);

                // when & then
                MvcResult result = mockMvc.perform(get("/board/{boardNo}", 1L))
                    .andDo(mvcResult -> {
                        // Debug: Print actual response details
                        System.out.println("Response Status: " + mvcResult.getResponse().getStatus());
                        System.out.println("View Name: " + mvcResult.getModelAndView().getViewName());
                        System.out.println("Model: " + mvcResult.getModelAndView().getModel());
                        if (mvcResult.getResolvedException() != null) {
                            System.out.println("Exception: " + mvcResult.getResolvedException());
                        }
                    })
                    .andExpect(status().isOk())
                    .andExpect(view().name("board/board"))
                    .andExpect(model().attributeExists("board"))
                    .andReturn();

                // Debug: Additional verification
                System.out.println("Final view name: " + result.getModelAndView().getViewName());
                System.out.println("Final model: " + result.getModelAndView().getModel());
            }
        }
    }
}
