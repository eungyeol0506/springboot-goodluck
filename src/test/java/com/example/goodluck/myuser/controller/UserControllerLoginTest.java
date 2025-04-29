package com.example.goodluck.myuser.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.example.goodluck.controller.UserController;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.global.MyFileHandler;
import com.example.goodluck.global.exception.myuser.UserLoginFaildException;
import com.example.goodluck.service.user.UserService;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerLoginTest {
    @MockBean                                                                                                                                                                                                                                                                                                                                    
    private UserService userService;
    @MockBean
    private MyFileHandler testFileHandler;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("유저 로그인 테스트")
    class LoginUser{

        @DisplayName("로그인 폼 받기 성공")
        @Test
        void successLoginForm() throws Exception{
            mockMvc.perform(get("/login"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("myuser/login"));
        }

        @DisplayName("로그인 성공")
        @Test
        void successLogin() throws Exception{
            //given
            MyUser myUser = MyUser.creatDummy(1L);
            String userId = myUser.getUserId();
            String userPw = myUser.getUserPw();
            BDDMockito.given(userService.loginUser(userId, userPw)).willReturn(myUser);
            //when // then
            mockMvc.perform(post("/login")
                            .param("userId", userId)
                            .param("userPw", userPw))
                    .andExpect(status().isOk())
                    .andExpect(view().name("home")) // 반환된 뷰 이름 검증
                    .andExpect(request().sessionAttribute("userNo", myUser.getUserNo())) // 세션 검증
                    // .andDo(print())
                    ;

            // 호출되었는지
            Mockito.verify(userService).loginUser(userId, userPw);
        }

        @Test
        @DisplayName("service 에서 UserLoginFaildException을 발생시키는 경우")
        void failLogin() throws Exception{
            // given 
            MyUser myUser = MyUser.creatDummy(1L);
            String userId = myUser.getUserId();
            String userPw = myUser.getUserPw();
            BDDMockito.given(userService.loginUser(userId, userPw))
                        .willThrow(new UserLoginFaildException("로그인 실패 테스트"));
            // when // then
            mockMvc.perform(post("/login")
                            .param("userId", userId)
                            .param("userPw", userPw))
                        .andExpect(status().isOk())
                        .andExpect(view().name("myuser/login"))
                        .andExpect(model().attribute("notice", "로그인 실패 테스트"))
                        .andExpect(request().sessionAttributeDoesNotExist("userNo"))
                        // .andDo(print())
                        ;

            Mockito.verify(userService).loginUser(userId, userPw);
        }

        @Test
        @DisplayName("다른 예외가 발생한 경우")
        void failElseException() throws Exception{
            // given 
            MyUser myUser = MyUser.creatDummy(1L);
            String userId = myUser.getUserId();
            String userPw = myUser.getUserPw();
            BDDMockito.given(userService.loginUser(userId, userPw))
                        .willThrow(new RuntimeException("로그인 실패 테스트"));
            // when // then
            mockMvc.perform(post("/login")
                            .param("userId", userId)
                            .param("userPw", userPw))
                        .andExpect(status().isOk())
                        .andExpect(view().name("error"))
                        .andExpect(request().sessionAttributeDoesNotExist("userNo"))
                        // .andDo(print())
                        ;

            Mockito.verify(userService).loginUser(userId, userPw);
        }
    }

    @Nested
    @DisplayName("로그아웃")
    class LogoutUser{
        @Test
        @DisplayName("로그아웃을 성공하는 경우")
        void successLogout() throws Exception{
            // given
            MyUser myUser = MyUser.creatDummy(1L);
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("userNo", myUser.getUserNo());
            // when // then
            mockMvc.perform(get("/logout")
                        .session(session))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(request().sessionAttributeDoesNotExist("userNo"))
                    .andDo(print());
        }
    }
}
