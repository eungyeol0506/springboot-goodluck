package com.example.goodluck.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.goodluck.domain.MyBoard;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.domain.AuthUser;
import com.example.goodluck.service.board.BoardService;
import com.example.goodluck.service.board.dto.BoardWriteRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
                MyUser writer = MyUser.builder()
                    .userNo(1L)
                    .userName("테스트 작성자")
                    .build();

                List<MyBoard> boards = Arrays.asList(
                    MyBoard.builder()
                        .boardNo(1L)
                        .boardTitle("테스트 제목1")
                        .contents("테스트 내용1")
                        .writer(writer)
                        .build(),
                    MyBoard.builder()
                        .boardNo(2L)
                        .boardTitle("테스트 제목2") 
                        .contents("테스트 내용2")
                        .writer(writer)
                        .build()
                );
                
                List<Integer> pages = Arrays.asList(1, 2, 3);
                
                BDDMockito.given(boardService.findAll(anyLong())).willReturn(boards);
                BDDMockito.given(boardService.getPageNumbers()).willReturn(pages);

                // when & then
                mockMvc.perform(get("/list/{page}", 1L))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("board/list"))
                    .andExpect(model().attributeExists("boards"))
                    .andReturn();

            }

            @Test
            @DisplayName("게시글 상세 조회 성공")
            void findById() throws Exception {
                // given
                MyUser writer = MyUser.builder()
                    .userNo(1L)
                    .userName("테스트 작성자")
                    .build();

                MyBoard board = MyBoard.builder()
                    .boardNo(1L)
                    .boardTitle("테스트 제목")
                    .contents("테스트 내용")
                    .writer(writer)
                    .build();
                
                BDDMockito.given(boardService.findBoardByNo(anyLong())).willReturn(board);

                // when & then
                mockMvc.perform(get("/board/{boardNo}", 1L))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("board/board"))
                    .andExpect(model().attributeExists("board"));
            }
        }
    }

    @Nested
    @DisplayName("게시글 작성")
    class Write {
        @Nested
        class Success {
            
            @Test
            @DisplayName("게시글 작성 폼 조회 성공")
            void getWriteForm() throws Exception {
                // given
                MyUser mockUser = MyUser.builder()
                    .userNo(1L)
                    .userId("testUser")
                    .userName("테스트유저")
                    .build();
                
                AuthUser authUser = getAuth(mockUser);

                // when & then
                mockMvc.perform(get("/board/write")
                        .with(authentication(
                            new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
                        )))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("board/write"))
                    ;
            }

            @Test
            @DisplayName("게시글 작성 성공")
            void postWrite() throws Exception {
                // given
                MockMultipartFile file = new MockMultipartFile(
                    "fileImage", 
                    "test.png", 
                    MediaType.IMAGE_PNG_VALUE,
                    "test image content".getBytes()
                );

                MyUser mockUser = MyUser.builder()
                    .userNo(1L)
                    .userId("testUser")
                    .userName("테스트유저")
                    .build();
                
                AuthUser authUser = getAuth(mockUser);
                
                Long newBoardNo = 1L;
                BDDMockito.given(boardService.write(any(MyUser.class), any(BoardWriteRequest.class), anyList()))
                    .willReturn(newBoardNo);

                // when & then
                mockMvc.perform(multipart("/board/write")
                        .file(file)
                        .with(authentication(
                            new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
                        ))
                        .param("boardTitle", "테스트 제목")
                        .param("contents", "테스트 내용"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/board/" + newBoardNo));
            }
        }

        @Nested
        class Failed {
            @Test
            @DisplayName("게시글 작성 실패 - 로그인하지 않은 경우")
            void writeWithoutLogin() throws Exception {
                mockMvc.perform(get("/board/write"))
                    .andExpect(status().is4xxClientError())
                    // 시큐리티에서 안된다고 탈취 해야함
                    ;
            }

            @Test
            @DisplayName("게시글 작성 실패 - 필수 필드 누락")
            void writeWithInvalidData() throws Exception {
                // given
                MyUser mockUser = MyUser.builder()
                    .userNo(1L)
                    .userId("testUser")
                    .userName("테스트유저")
                    .build();
                
                AuthUser authUser = getAuth(mockUser);

                // when & then
                mockMvc.perform(multipart("/board/write")
                        .with(authentication(
                            new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
                        ))
                        .param("boardTitle", "") // empty title
                        .param("contents", "테스트 내용"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("board/write"))
                    .andExpect(model().attributeExists("notice"));
            }
        }
    }

    @Nested
    @DisplayName("게시글 수정")
    class Modify {
        @Nested
        class Success {
            @Test
            @DisplayName("게시글 수정 폼 조회 성공")
            void getModifyForm() throws Exception {
                // given
                MyUser mockUser = MyUser.builder()
                    .userNo(1L)
                    .userId("testUser")
                    .userName("테스트유저")
                    .build();

                MyBoard mockBoard = MyBoard.builder()
                    .boardNo(1L)
                    .boardTitle("Original Title")
                    .contents("Original Content")
                    .writer(mockUser)
                    .build();

                AuthUser authUser = getAuth(mockUser);
                
                BDDMockito.given(boardService.findBoardByNo(anyLong())).willReturn(mockBoard);

                // when & then
                mockMvc.perform(get("/board/modify/{boardNo}", 1L)
                        .with(authentication(
                            new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
                        )))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("board/modify"))
                    .andExpect(model().attributeExists("modifyData"));
            }

            @Test
            @DisplayName("게시글 수정 성공 - 첨부파일 포함")
            void postModifyWithAttachments() throws Exception {
                // given
                MyUser mockUser = MyUser.builder()
                    .userNo(1L)
                    .userId("testUser")
                    .userName("테스트유저")
                    .build();

                MyBoard mockBoard = MyBoard.builder()
                    .boardNo(1L)
                    .boardTitle("Original Title")
                    .contents("Original Content")
                    .writer(mockUser)
                    .build();

                MockMultipartFile newFile = new MockMultipartFile(
                    "fileImage", 
                    "test.png", 
                    MediaType.IMAGE_PNG_VALUE,
                    "test image content".getBytes()
                );

                AuthUser authUser = getAuth(mockUser);
                
                BDDMockito.given(boardService.findBoardByNo(anyLong())).willReturn(mockBoard);
                BDDMockito.given(boardService.modify(any(), anyList(), anyList())).willReturn(1L);

                // when & then
                mockMvc.perform(multipart("/board/modify/{boardNo}", 1L)
                        .file(newFile)
                        .with(authentication(
                            new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
                        ))
                        .param("boardNo", "1")
                        .param("boardTitle", "Modified Title")
                        .param("contents", "Modified Content")
                        .param("deleteImageNo", "2", "3"))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/board/1"));
            }
        }

        @Nested
        class Failed {
            @Test
            @DisplayName("게시글 수정 실패 - 권한 없음")
            void modifyWithoutPermission() throws Exception {
                // given
                MyUser mockUser = MyUser.builder()
                    .userNo(1L)
                    .userId("testUser")
                    .userName("테스트유저")
                    .build();

                MyUser differentUser = MyUser.builder()
                    .userNo(2L)
                    .userId("differentUser")
                    .userName("다른유저")
                    .build();

                MyBoard mockBoard = MyBoard.builder()
                    .boardNo(1L)
                    .boardTitle("Original Title")
                    .contents("Original Content")
                    .writer(differentUser)  // Different user is the writer
                    .build();

                AuthUser authUser = getAuth(mockUser);
                
                BDDMockito.given(boardService.findBoardByNo(anyLong())).willReturn(mockBoard);

                // when & then
                mockMvc.perform(get("/board/modify/{boardNo}", 1L)
                        .with(authentication(
                            new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
                        )))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/board/1"));
            }

            @Test
            @DisplayName("게시글 수정 실패 - 로그인하지 않은 경우")
            void modifyWithoutLogin() throws Exception {
                mockMvc.perform(get("/board/modify/{boardNo}", 1L))
                    .andExpect(status().is4xxClientError());
            }

            @Test
            @DisplayName("게시글 수정 실패 - 필수 필드 누락")
            void modifyWithInvalidData() throws Exception {
                // given
                MyUser mockUser = MyUser.builder()
                    .userNo(1L)
                    .userId("testUser")
                    .userName("테스트유저")
                    .build();

                MyBoard mockBoard = MyBoard.builder()
                    .boardNo(1L)
                    .boardTitle("Original Title")
                    .contents("Original Content")
                    .writer(mockUser)
                    .build();

                AuthUser authUser = getAuth(mockUser);
                
                BDDMockito.given(boardService.findBoardByNo(anyLong())).willReturn(mockBoard);

                // when & then
                mockMvc.perform(multipart("/board/modify/{boardNo}", 1L)
                        .with(authentication(
                            new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
                        ))
                        .param("boardNo", "1")
                        // Missing required boardTitle
                        .param("contents", "Modified Content"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("board/modify"))
                    ;
            }
        }
    }

    private AuthUser getAuth(MyUser mockUser) {
        // AuthUser 생성
        AuthUser authUser = new AuthUser(mockUser);
        // 인증 객체 주입
        Authentication auth = new UsernamePasswordAuthenticationToken(
            authUser,
            null,
            authUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        return authUser;
    }

}
