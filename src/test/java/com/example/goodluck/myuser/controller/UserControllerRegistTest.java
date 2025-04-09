package com.example.goodluck.myuser.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.api.Assertions;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.FlashMap;

import com.example.goodluck.common.MyFileHandler;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.exception.myuser.UserRegistFaildException;
import com.example.goodluck.myuser.UserController;
import com.example.goodluck.myuser.UserService;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerRegistTest {
    @MockBean                                                                                                                                                                                                                                                                                                                                    
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

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
                MyUser myUser = MyUser.creatDummy(1L);
                MultiValueMap<String,String> registForms = createRegistForm(myUser);

                BDDMockito.given(userService.registUser(any(MyUser.class))).willReturn(myUser);
                // when //then
                mockMvc.perform(MockMvcRequestBuilders.multipart("/regist")
                                                    .params(registForms)
                                                    .contentType(MediaType.MULTIPART_FORM_DATA))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/login"))
                        // .andDo(print())
                        // .andReturn()
                        ;
                
                Mockito.verify(userService).registUser(any(MyUser.class));                              
             }

            @Test
            @DisplayName("회원가입을 성공하는 경우 - 이미지있음")
            void successRegistUserWithProfile() throws Exception{
                // given
                MyUser myUser = MyUser.creatDummy(1L);
                MockMultipartFile multipartFile = new MockMultipartFile("fileImage", "new_profile.img", "image/png", "test-image-content".getBytes());
                MultiValueMap<String,String> registForms = createRegistForm(myUser);
                
                BDDMockito.given(userService.registUser(any(MyUser.class))).willReturn(myUser);
                // when //then
                mockMvc.perform(MockMvcRequestBuilders.multipart("/regist")
                                                        .file(multipartFile)
                                                        .params(registForms)
                                                        .contentType(MediaType.MULTIPART_FORM_DATA))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/login"))
                        ;

                Mockito.verify(userService).registUser(any(MyUser.class));                              
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase{
            @Test
            @DisplayName("중복된 아이디를 입력하는 경우")
            void failDuplicatedUserId() throws Exception{
                // given
                MyUser myUser = MyUser.creatDummy(1L);
                
                MultiValueMap<String, String> registForms = createRegistForm(myUser);
                BDDMockito.given(userService.registUser(any(MyUser.class))).willThrow(new UserRegistFaildException("중복 아이디", myUser));

                // when then
                // MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/regist")
                mockMvc.perform(MockMvcRequestBuilders.multipart("/regist")
                                                .params(registForms)
                                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                        .andExpect(status().isOk())
                                        .andExpect(view().name("myuser/regist_form"))
                                        .andExpect(model().attributeExists("preValue"))
                                        .andExpect(model().attribute("notice", "중복 아이디"))
                                        // .andDo(print())
                                        ;
                // Assertions.assertThat(mvcResult.getFlashMap().get("notice")).isEqualTo("중복 아이디");
                Mockito.verify(userService).registUser(any(MyUser.class));
            }

            @Test
            @DisplayName("필수값이 입력되지 않은 경우")
            void failRequiredInputNotPresent() throws Exception{
                // given
                MyUser myUser = MyUser.creatDummy(1L);
                myUser.setUserId(null);
                // myUser.setUserEmail(null);
                MultiValueMap<String, String> registForms = createRegistForm(myUser);
                BDDMockito.given(userService.registUser(any(MyUser.class))).willReturn(myUser);

                // when then
                // MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/regist")
                mockMvc.perform(MockMvcRequestBuilders.multipart("/regist")
                                                .params(registForms)
                                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                        .andExpect(status().isOk())
                                        .andExpect(view().name("myuser/regist_form"))
                                        .andExpect(model().attributeExists("notice"))
                                        .andExpect(model().attributeExists("preValue"))
                                        .andDo(print())
                                        // .andReturn()
                                        ;
                // Map<String, Object> modelAndView = mvcResult.getModelAndView().getModel();            
                // Assertions.assertThat(modelAndView.get("notice")).isNotNull();
                
                Mockito.verify(userService, Mockito.never()).registUser(any(MyUser.class));
            }
            @Test
            @DisplayName("컨트롤러 외 예외가 발생하는 경우")
            // 어캐 처리하지
            void failDataAccessException() throws Exception{
                // given
                MyUser myUser = MyUser.creatDummy(1L);
                MultiValueMap<String, String> registForms = createRegistForm(myUser);
                BDDMockito.given(userService.registUser(any(MyUser.class))).willThrow(new RuntimeException("새로운 예외 발생"));

                // when then
                mockMvc.perform(MockMvcRequestBuilders.multipart("/regist")
                                    .params(registForms)
                                    .contentType(MediaType.MULTIPART_FORM_DATA))
                        .andExpect(status().isOk())
                        .andExpect(view().name("error"))
                        .andDo(print())
                        .andReturn();
            }

            @Test
            @DisplayName("잘못된 파일 데이터를 받은 경우")
            void failInvalidMultipartFile() throws Exception{
                // given4
                MyUser myUser = MyUser.creatDummy(1L);
                MockMultipartFile multipartFile = new MockMultipartFile("fileImage", "faild_file.img", "image/png", "test-image-content".getBytes());
                
                MultiValueMap<String,String> registForms = createRegistForm(myUser);

                BDDMockito.given(userService.registUser(any(MyUser.class))).willReturn(myUser);
                // when then
                mockMvc.perform(MockMvcRequestBuilders.multipart("/regist")
                                                        .file(multipartFile)
                                                        .params(registForms)
                                                        .contentType(MediaType.MULTIPART_FORM_DATA))
                                            .andExpect(status().isOk())
                                            .andExpect(view().name("myuser/regist_form"))
                                            .andExpect(model().attributeExists("preValue"))
                                            .andDo(print());

                Mockito.verify(userService, Mockito.never()).registUser(any(MyUser.class));
            }
        }
    }
}
