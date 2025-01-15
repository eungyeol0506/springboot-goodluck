package com.example.goodluck.myuser;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.assertj.MockMvcTester.MockMvcRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.FlashMap;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.exception.UserNotFoundLoginException;



@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @MockBean                                                                                                                                                                                                                                                                                                                                    
    private UserService userService;

    // @Mock
    // private JdbcTemplateUserRepository mockUserRepository;

    @Autowired
    private MockMvc mockMvc;

    MyUser myUser = new MyUser();
    @BeforeEach
    void setUp(){
        // 테스트용으로 사용할 myUser 데이터
        myUser.setUserNo(Long.valueOf(123456));
        myUser.setUserId("test");
        myUser.setUserPw("testtest");
        myUser.setUserName("테스터");
        myUser.setUserEmail("test@test.com");
        myUser.setPostNo("12345");
        myUser.setProfileImgName("controller_test.png");
        myUser.setProfileImgPath("controller_test_path");
    }

    @Nested
    @DisplayName("로그인")
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
            String userId = myUser.getUserId();
            String userPw = myUser.getUserPw();
            BDDMockito.given(userService.loginUser(userId, userPw)).willReturn(myUser);
            //when // then
            mockMvc.perform(post("/login")
                        .param("userId", userId)
                        .param("userPw", userPw))
                    .andExpect(status().isOk())
                    .andExpect(view().name("home")) // 반환된 뷰 이름 검증
                    .andExpect(request().sessionAttribute("userNo", myUser.getUserNo()))
                    .andDo(print());

            Mockito.verify(userService).loginUser(userId, userPw);
        }

        @Test
        @DisplayName("로그인 실패")
        void failLogin() throws Exception{
            // given 
            String userId = myUser.getUserId();
            String userPw = myUser.getUserPw();
            BDDMockito.given(userService.loginUser(userId, userPw)).willThrow(new UserNotFoundLoginException("로그인 실패"));
            // when // then
            MvcResult mvcResult = mockMvc.perform(post("/login")
                                        .param("userId", userId)
                                        .param("userPw", userPw))
                                    .andExpect(status().is3xxRedirection())
                                    .andExpect(redirectedUrl("/login"))
                                    // .andExpect(model().attribute("message", "로그인 실패"))
                                    // .andDo(print());
                                    .andReturn();
                                    
            FlashMap flashMap = mvcResult.getFlashMap();
            Assertions.assertThat(flashMap.get("message")).isEqualTo("로그인 실패");        

        }
    }
    @Nested
    @DisplayName("로그아웃")
    class LogoutUser{
        @Test
        @DisplayName("로그아웃을 성공하는 경우")
        void successLogout() throws Exception{
            // given
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("userNo", myUser.getUserNo());
            // when // then
            mockMvc.perform(get("/logout")
                        .session(session))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(request().sessionAttributeDoesNotExist("userNo"))
                    .andDo(print());
        // 로그아웃에 실패하는 경우???
        }
    }

    @Nested
    @DisplayName("회원가입")
    class RegistUser{
        // given 공통
        private MultiValueMap<String,String> createRegistForm(MyUser user) {
            MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
            // formParams.add("userNo", String.valueOf(user.getUserNo()));
            formParams.add("userName", user.getUserName());
            formParams.add("userId", user.getUserId());
            formParams.add("userPw", user.getUserPw());
            formParams.add("userEmail", user.getUserEmail());
            formParams.add("telNo", user.getTelNo());
            formParams.add("postNo", user.getPostNo());
            formParams.add("addressMain", user.getAddressMain());
            formParams.add("addresDetail", user.getAddressDetail());
            return formParams;
        } 
        @Nested
        class SuccessCase{
            @Test
            @DisplayName("회원가입을 성공하는 경우 - 이미지없음")
            void successRegistUser() throws Exception{
                // given
                MultiValueMap<String,String> registForms = createRegistForm(myUser);

                BDDMockito.given(userService.registUser(any(MyUser.class))).willReturn(myUser);
                // when //then
                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/regist")
                                                // .file(null)
                                                .params(registForms)
                                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                            .andExpect(status().is3xxRedirection())
                                            .andExpect(redirectedUrl("/login"))
                                            .andDo(print())
                                            .andReturn();
                
                FlashMap flashMap = mvcResult.getFlashMap();
                Assertions.assertThat(flashMap.get("message")).isEqualTo("회원가입에 성공하였습니다.");
                Mockito.verify(userService).registUser(any(MyUser.class));                              
             }
            @Test
            @DisplayName("회원가입을 성공하는 경우 - 이미지있음")
            void successRegistUserWithProfile() throws Exception{
                // given
                MockMultipartFile multipartFile = new MockMultipartFile("fileImage", "new_profile.img", "image/png", "test-image-content".getBytes());
                MultiValueMap<String,String> registForms = createRegistForm(myUser);
                BDDMockito.given(userService.registUser(any(MyUser.class))).willReturn(myUser);
                // when //then
                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/regist")
                                                .file(multipartFile)
                                                .params(registForms)
                                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                        .andExpect(status().is3xxRedirection())
                                        .andExpect(redirectedUrl("/login"))
                                        .andDo(print())
                                        .andReturn();

                FlashMap flashMap = mvcResult.getFlashMap();
                Assertions.assertThat(flashMap.get("message")).isEqualTo("회원가입에 성공하였습니다.");
                Mockito.verify(userService).registUser(any(MyUser.class));                              
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase{
            @Test
            @DisplayName("중복된 아이디를 입력하는 경우")
            void failDuplicatedUserId(){
                // given
                // when then
            }
            @Test
            @DisplayName("필수값이 입력되지 않은 경우")
            void failRequiredInputNotPresent(){
                // given
                // when then
            }
            @Test
            @DisplayName("DB 접근 시 예외가 발생한 경우")
            void failDataAccessException(){
                // given
                // when then
            }
            @Test
            @DisplayName("잘못된 파일 데이터를 받은 경우")
            void failInvalidMultipartFile(){
                // given
                // when then
            }
        }
    }

    @Nested
    @DisplayName("회원정보 수정")
    class EditUser{
        // given 공통
        MockMultipartFile multipartFile = new MockMultipartFile("fileImage", "profile.img", "image/png", "test-image-content".getBytes());
        // MyUser myUser = getTestNewUser();
        private MultiValueMap<String, String> creaetEditForm(MyUser user){
            MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
            formParams.add("userNo", String.valueOf(user.getUserNo()));
            formParams.add("userId", user.getUserId());
            formParams.add("userName", user.getUserName());
            formParams.add("userPw", user.getUserPw());
            formParams.add("userEmail", user.getUserEmail());
            formParams.add("telNo", user.getTelNo());
            formParams.add("postNo", user.getPostNo());
            formParams.add("addressMain", user.getAddressMain());
            formParams.add("addresDetail", user.getAddressDetail());
            return formParams;
        }
        
        @Nested
        class SuccessCase{
            @DisplayName("회원 정보 수정에 성공한 경우 - 이미지 있음")
            @Test
            void successPostUserEdit(){
                // given
                MultiValueMap<String, String> editedForms = creaetEditForm(myUser);

                Mockito.when(userService.getUserInfo(any(Long.class))).thenReturn(myUser);
                Mockito.when(userService.updateUser(any(MyUser.class))).thenReturn(myUser);
                // when // then
                try {
                    mockMvc.perform(MockMvcRequestBuilders.multipart("/mypage/edit")
                                                        .file(multipartFile)
                                                        .params(editedForms)
                                                        .contentType(MediaType.MULTIPART_FORM_DATA))
                                    // .andDo(print())
                                    .andExpect(status().is3xxRedirection())
                                    .andExpect(redirectedUrl("/mypage"));

                    Mockito.verify(userService).updateUser(any(MyUser.class));
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("테스트 중 예외 발생: " + e.getMessage());
                }
                                
            }
        }
        @Nested
        @DisplayName("회원정보 수정 실패 케이스")
        class FailCase{
            @DisplayName("입력값이 옳지 않은 경우")
            @Test
            void invalidRequestData(){

            }
            @DisplayName("올바르지 않은 파일데이터인 경우")
            @Test
            void invalidFileData(){

            }
            @DisplayName("서비스객체에서 오류가 난 경우")
            @Test
            void occuredException(){}

        }
    }

    @Nested
    @DisplayName("회원 정보 보기")
    class GetUserInfo{
        @Nested
        class SuccessCase{
            @Test
            @DisplayName("회원정보 불러오기에 성공한 경우")
            void successGetUserInfo() throws Exception{
                // given
                MockHttpSession session = new MockHttpSession();
                session.setAttribute("userNo", myUser.getUserNo());
                BDDMockito.given(userService.getUserInfo(myUser.getUserNo())).willReturn(myUser);
                // when & then
                mockMvc.perform(get("/mypage")
                            .session(session))
                        .andExpect(status().isOk())
                        .andExpect(view().name("myuser/mypage"))
                        .andExpect(request().sessionAttribute("userNo", myUser.getUserNo()))
                        .andExpect(model().attribute("user", myUser))
                        .andDo(print()); 
                
                Mockito.verify(userService).getUserInfo(myUser.getUserNo());
            }
        }
        @Nested
        class FailCase{

        }
    }
}
