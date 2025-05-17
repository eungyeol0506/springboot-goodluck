package com.example.goodluck.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

import com.example.goodluck.domain.AuthUser;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.service.user.UserError;
import com.example.goodluck.service.user.UserService;
import com.example.goodluck.service.user.UserServiceException;
import com.example.goodluck.service.user.dto.UserLoginRequest;
import com.example.goodluck.service.user.dto.UserPwChangeRequest;
import com.example.goodluck.service.user.dto.UserRegistRequest;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/"))
                        ;

            }

            @Test
            void successLogout() throws Exception{
                MockHttpSession session = new MockHttpSession();
                
                mockMvc.perform(get("/logout")
                                .session(session))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"))       
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
        void successGetUserInfo() throws Exception{
            // given
            Long fakeUserNo = 456L;
            MyUser mockUser = MyUser.builder()
                                .userNo(fakeUserNo)
                                .userId("12345")
                                .userPw("tttt")
                                .userName("테스트")
                                .userEmail("test@test.com")
                                .build();

            // userService가 이 userNo로 유저 조회 시 mockUser 반환
            BDDMockito.given(userService.getUser(anyLong())).willReturn(mockUser);

            // AuthUser 생성
            AuthUser authUser = getAuth(mockUser);

            // when then
            mockMvc.perform(get("/profile")
                    .with(authentication(
                            new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
                        )))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("user/profile"))
                    .andExpect(model().attributeExists("user"))
                    ;
        }

    }

    @Nested
    class Regist{

        @Nested
        class Success{

            @Test
            void successGetRegist() throws Exception{
                mockMvc.perform(get("/regist"))
                        .andDo(print())
                        .andExpect(view().name("user/regist"))
                        ;
            }

            @Test
            void successPostRegist() throws Exception{
                // given
                MockMultipartFile file = new MockMultipartFile("fileImage", "test.png", MediaType.IMAGE_PNG_VALUE,"dummy-content".getBytes());
                
                BDDMockito.given(userService.regist(any(UserRegistRequest.class), any(MultipartFile.class))).willReturn(456L);
                
                // when then
                mockMvc.perform(MockMvcRequestBuilders.multipart("/regist")
                                .file(file)
                                .param("userEmail","test@example.com")
                                .param("userId","teeeestttt")
                                .param("userPw","teeeestttt")
                                .param("userName","teeeestttt")
                                )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/"))
                        ;
                
            }
        }
        
        @Nested
        class Failed{
            
            @Test
            @DisplayName("@Valid 검증 예외 처리")
            void faildValidation() throws Exception{
                // given
                MockMultipartFile file = new MockMultipartFile("fileImage", "test.png", MediaType.IMAGE_PNG_VALUE,"dummy-content".getBytes());
                BDDMockito.given(userService.regist(any(UserRegistRequest.class), any(MultipartFile.class))).willReturn(456L);

                // when then
                mockMvc.perform(MockMvcRequestBuilders.multipart("/regist")
                                .file(file)
                                .param("userEmail","test@example.com")
                                .param("userId","")
                                .param("userPw","teeeestttt")
                                .param("userName","teeeestttt")
                                )
                                .andExpect(status().isOk())
                                .andExpect(view().name("user/regist"))
                                .andExpect(model().attributeExists("notice"))
                                ;
            }

            @Test
            @DisplayName("중복되는 아이디 예외 처리")
            void failedDuplicatedId() throws Exception{
                // given
                MockMultipartFile file = new MockMultipartFile("fileImage", "test.png", MediaType.IMAGE_PNG_VALUE,"dummy-content".getBytes());
                MyUser test = MyUser.builder().userId("test").userPw("testt").build();
                BDDMockito.given(userService.regist(any(UserRegistRequest.class), any(MultipartFile.class))).willThrow(new UserServiceException(UserError.USER_ID_DUPLICATED, test));
                
                // when then
                mockMvc.perform(MockMvcRequestBuilders.multipart("/regist")
                                .file(file)
                                .param("userEmail","test@example.com")
                                .param("userId","testtttt")
                                .param("userPw","teeeestttt")
                                .param("userName","teeeestttt")
                                )
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(view().name("user/regist"))
                                .andExpect(model().attribute("notice", UserError.USER_ID_DUPLICATED.getMessage()))
                                .andExpect(model().attributeExists("registRequest"))
                                ;
            }
        }
    }
    
    @Nested
    class Update{

        @Nested
        class Success{

            @Test
            void successGetProfileForm() throws Exception{
                // given
                // 세션 객체에 userNo 삽입
                Long fakeUserNo = 456L;
                MyUser mockUser = MyUser.builder()
                                    .userNo(fakeUserNo)
                                    .userId("12345")
                                    .userPw("tttt")
                                    .userName("테스트")
                                    .userEmail("test@test.com")
                                    .build();

                // userService가 이 userNo로 유저 조회 시 mockUser 반환
                BDDMockito.given(userService.getUser(anyLong())).willReturn(mockUser);

                // AuthUser 생성
                AuthUser authUser = getAuth(mockUser);
    
                // when & then
                mockMvc.perform(get("/profile/form")
                        .with(authentication(
                            new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
                        )))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("user/edit"))
                    .andExpect(model().attributeExists("editRequest"))
                    .andExpect(model().attributeExists("notice"));
            }

            
            @Test
            void successPostUpdate() throws Exception{
                // given
                MockMultipartFile file = new MockMultipartFile(
                "fileImage", "profile.png", "image/png", "dummy".getBytes());
                // given
                // 세션 객체에 userNo 삽입
                Long fakeUserNo = 456L;
                MyUser mockUser = MyUser.builder()
                                    .userNo(fakeUserNo)
                                    .userId("12345")
                                    .userPw("tttt")
                                    .userName("테스트")
                                    .userEmail("test@test.com")
                                    .build();

                // userService가 이 userNo로 유저 조회 시 mockUser 반환
                BDDMockito.given(userService.getUser(anyLong())).willReturn(mockUser);

                // AuthUser 생성
                AuthUser authUser = getAuth(mockUser);

                // when & then
                mockMvc.perform(multipart("/profile/form")
                        .file(file)
                        .with(authentication(
                            new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
                        ))
                        .param("userName", "테스트")
                        .param("userEmail", "eunji@test.com"))
                    .andExpect(status().is3xxRedirection()) // redirect
                    .andExpect(redirectedUrl("/profile"));   // 리다이렉트 대상 URL

            }

            /*
             * 비밀번호 변경
             */
            @Test
            void successGetPasswordChange() throws Exception{
                // given
                Long fakeUserNo = 456L;
                MyUser mockUser = MyUser.builder()
                                    .userNo(fakeUserNo)
                                    .userId("12345")
                                    .userPw("tttt")
                                    .userName("테스트")
                                    .userEmail("test@test.com")
                                    .build();

                AuthUser authUser = getAuth(mockUser);
    
                // when & then
                mockMvc.perform(get("/password-change")
                        .with(authentication(
                            new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
                        )))
                    .andExpect(status().isOk())
                    .andExpect(view().name("user/password-change"))
                    ;
            }


            @Test
            void successPostPasswordChange() throws Exception{
                // given
                Long fakeUserNo = 456L;
                String oldPw = "12345678";
                String newPw = "12345679";

                MyUser mockUser = MyUser.builder()
                                    .userNo(fakeUserNo)
                                    .userId("12345")
                                    .userPw(oldPw)
                                    .userName("테스트")
                                    .userEmail("test@test.com")
                                    .build();
                
                AuthUser authUser = getAuth(mockUser);
                // when then
                mockMvc.perform(post("/password-change")
                        .with(authentication(
                            new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
                        ))
                        .param("oldPw", oldPw)
                        .param("newPw", newPw)
                        .param("newPwCfm", newPw))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login"))
                    ;
            }
        }
        
        @Nested
        class Failed{

            @Test
            @DisplayName("DTO Validation 예외 처리")
            void faildValidation() throws Exception{
                // given
                MockMultipartFile file = new MockMultipartFile(
                "fileImage", "profile.png", "image/png", "dummy".getBytes());
                MockHttpSession session = new MockHttpSession();
                
                setSecurityContext(session);

                mockMvc.perform(multipart("/profile/form")
                        .file(file)
                        .session(session)
                        .param("userName", "")
                        .param("userEmail", "eunji@test.com"))
                    .andExpect(status().isOk()) // redirect
                    .andExpect(view().name("user/edit"))
                    .andExpect(model().attributeExists("notice"))
                    ;  
                
            }
        
            @Test
            @DisplayName("비밀번호 로직에 걸려 예외 발생")
            void failedTryChangeSamePassword() throws Exception{
                // given
                Long fakeUserNo = 456L;
                String oldPw = "12345678";
                String newPw = "12345679";

                MyUser mockUser = MyUser.builder()
                                    .userNo(fakeUserNo)
                                    .userId("12345")
                                    .userPw(oldPw)
                                    .userName("테스트")
                                    .userEmail("test@test.com")
                                    .build();
                
                AuthUser authUser = getAuth(mockUser);

                BDDMockito.willThrow(new UserServiceException(UserError.IS_NOT_USER_PW))
                    .given(userService)
                    .changePw(anyLong(), any(UserPwChangeRequest.class));


                // when then
                mockMvc.perform(post("/password-change")
                        .with(authentication(
                            new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
                        ))
                        .param("oldPw", oldPw)
                        .param("newPw", newPw)
                        .param("newPwCfm", newPw))
                    .andExpect(status().isOk())
                    .andExpect(view().name("user/password-change"))
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

    private void setSecurityContext(MockHttpSession session) {
        UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken("testUser", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }
    
}
