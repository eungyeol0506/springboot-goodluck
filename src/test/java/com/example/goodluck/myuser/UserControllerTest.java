package com.example.goodluck.myuser;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester.MockMvcRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.goodluck.domain.MyUser;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;


@WebMvcTest(controllers = UserController.class)
// @ActiveProfiles("test")
public class UserControllerTest {

    @MockBean                                                                                                                                                                                                                                                                                                                                               
    private UserService userService;

    // @InjectMocks
    // private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("로그인 응답 테스트")
    class LoginUser{
        MyUser testMyUser = getTestNewUser();
        String id = "test";
        String pw = "test";
        
        @DisplayName("로그인 폼 받기 성공")
        @Test
        void LoginSuccessCase1() throws Exception{
            mockMvc.perform(get("/login"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("myuser/login"));
        }
        @DisplayName("로그인 성공")
        @Test
        void LoginSuccessCase2() throws Exception{
            //given
            given(userService.loginUserService("test", "test"))
                .willReturn(Optional.of(new MyUser()));
            //when
            // when(userService.loginUserService("test", "test")).thenReturn()
            mockMvc.perform(post("/login")
                                .param("id", id)
                                .param("pw", pw))
            //then
                    // .andExpect(status().is3xxRedirection())
                    // .andExpect(redirectedUrl("/")) //redirect Url 검증
                    .andExpect(view().name("home")) // 반환된 뷰 이름 검증
                    ;
            // verify(null)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
        }

        @Test
        @DisplayName("로그인 실패")
        void LoginFailCase() throws Exception{
            // given 
            given(userService.loginUserService("test", "test")).willThrow(new IllegalStateException("존재하지 않는 정보입니다."));

            // when
            mockMvc.perform(post("/login")
                            .param("id", id)
                            .param("pw",pw))
            // then
                    .andExpect(status().isOk())
                    // .andExpect(redirectedUrlPattern("/login*"))
                    .andExpect(model().attribute("message", "존재하지 않는 정보입니다."))
                    ;
        }
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class RegistUser{
        @Test
        @DisplayName("회원가입 성공 케이스")
        void RegistSuccessCase() throws Exception{
            MyUser newUser = getTestNewUser();
            given(userService.registUser(any(MyUser.class))).willReturn(Optional.of(newUser));
            
            String jsonRequst = """
                {
                    "userName" : "%s",
                    "userId" : "%s",
                    "userPw" : "%s",
                    "userEmail" : "%s"
                }       
                """.formatted(newUser.getUserName(), newUser.getUserId(), newUser.getUserPw(), newUser.getUserEmail());

            mockMvc.perform(post("/regist")
                            .contentType("application/json")
                            .content(jsonRequst))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/login"));                         
        }

        @Test
        @DisplayName("회원가입 실패 케이스 - 아이디가 중복됨")
        void RegistFailCase1() throws Exception{
            MyUser newUser = getTestNewUser();
            given(userService.registUser(any(MyUser.class))).willThrow(new IllegalStateException("중복되는 아이디 입니다."));
            String jsonRequst = """
                {
                    "userName" : "%s",
                    "userId" : "%s",
                    "userPw" : "%s",
                    "userEmail" : "%s"
                }       
                """.formatted(newUser.getUserName(), newUser.getUserId(), newUser.getUserPw(), newUser.getUserEmail());
                
            mockMvc.perform(post("/regist")
                            .contentType("application/json")
                            .content(jsonRequst))
                        .andExpect(status().isOk())
                        .andExpect(model().attribute("message", "중복되는 아이디 입니다."));     
        }

        @Test
        @DisplayName("회원가입 실패 케이스 - 필수 값이 입력안됨")
        void RegistFailCase2() throws Exception{
            MyUser newUser = getTestNewUser();
            given(userService.registUser(any(MyUser.class))).willReturn(Optional.of(newUser));
            
            String jsonRequst = """
                {
                    "userName" : "%s",
                    "userId" : "%s",
                    "userEmail" : "%s"
                }       
                """.formatted(  newUser.getUserName(), 
                                newUser.getUserId(), 
                                newUser.getUserEmail());
                mockMvc.perform(post("/regist")
                                .contentType("application/json")
                                .content(jsonRequst))
                            .andExpect(status().isOk())
                            .andExpect(model().attribute("message", "must not be blank"));
        }

    }


    private MyUser getTestNewUser() {
        MyUser newUser = new MyUser();
        newUser.setUserNo(Long.valueOf(123456));
        newUser.setUserId("test");
        newUser.setUserPw("test");
        newUser.setUserName("테스터");
        newUser.setUserName("test@test.com");
        return newUser;
    }
}
