package com.example.goodluck.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.service.user.UserError;
import com.example.goodluck.service.user.UserService;
import com.example.goodluck.service.user.UserServiceException;
import com.example.goodluck.service.user.dto.UserLoginRequest;
import com.example.goodluck.service.user.dto.UserRegistRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    
    @Autowired private MockMvc mockMvc;
    @MockBean UserService userService;

    @Nested
    class Login{

        @Nested
        class Success{
            @Test
            void successGetLogin() throws Exception{
                mockMvc.perform(get("/login"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(view().name("user/login"))
                        .andExpect(model().attributeExists("loginRequest"))
                        ;
                        
            }

            @Test
            void successPostLogin() throws Exception{
                // given
                String username = "테스트";
                String password = "testtest";

                MyUser mockUser = MyUser.builder()
                                    .userNo(1L)
                                    .build();
                
                BDDMockito.given(userService.login(any(UserLoginRequest.class))).willReturn(mockUser);

                // when & then
                mockMvc.perform(post("/login")
                                .param("username",username)
                                .param("password", password))
                        .andExpect(status().isOk())
                        .andExpect(view().name("home"))
                        .andExpect(request().sessionAttribute("userNo", mockUser.getUserNo()))
                        ;
            }

            @Test
            void successLogout() throws Exception{
                MockHttpSession session = new MockHttpSession();
                
                mockMvc.perform(get("/logout")
                                .session(session))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("home"))       
                    .andExpect(request().sessionAttributeDoesNotExist("userNo"))
                    ;
            }
        }        

        @Nested
        class Failed{
            @Test
            @DisplayName("DTO 검증에 실패함")
            void faildValidation() throws Exception{
                // given
                String username = "테스트";
                String password = "";

                // when & then
                mockMvc.perform(post("/login")
                                .param("username",username)
                                .param("password", password))
                        .andExpect(status().isOk())
                        .andExpect(view().name("user/login"))
                        .andExpect(model().attributeExists("notice"))
                        ;
            }

            @Test
            @DisplayName("로그인에 실패함 - 비즈니스 로직에서 예외 발생")
            void failedLoginByNoutFound() throws Exception{
                // given
                String username = "nothing";
                String password = "12341234";

                BDDMockito.given(userService.login(any(UserLoginRequest.class))).willThrow(new UserServiceException(UserError.USER_NOT_FOUND));
                // when then
                mockMvc.perform(post("/login")
                                .param("username",username)
                                .param("password",password))
                        .andExpect(view().name("user/login"))
                        .andExpect(model().attribute("notice", UserError.USER_NOT_FOUND.getMessage()))
                        ;
            }
        }
    }

    @Nested
    class Find{
        @Test
        void successGetUserInfo(){

        }
    }

    @Nested
    class Regist{

        @Nested
        class Success{

            @Test
            void successGetRegist() throws Exception{
                
            }

            @Test
            void successPostRegist() throws Exception{
                // given
                MockMultipartFile file = new MockMultipartFile("fileImage", "test.png", MediaType.IMAGE_PNG_VALUE,"dummy-content".getBytes());
                UserRegistRequest param = new UserRegistRequest();
                param.setUserEmail("test@example.com");
                param.setUserId("teeeestttt");
                param.setUserPw("teeeestttt");
                param.setUserName("테스트");
    
                // when then
                // mockMvc.perform(null)
                mockMvc.perform(MockMvcRequestBuilders.multipart("/regist")
                                .file(file)
                                .param("requestData.userEmail","test@example.com")
                                .param("requestData.userId","teeeestttt")
                                .param("requestData.userPw","teeeestttt")
                                .param("requestData.userName","teeeestttt")
                                )
                                .andExpect(status().isOk())
                                .andExpect(view().name("home"));
                
            }
        }
    }
}
