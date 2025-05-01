package com.example.goodluck.service.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.service.user.dto.UserRegistRequest;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired UserService userService;

    @Test
    @DisplayName("회원 가입 성공")
    void successRegist(){
        // given
        UserRegistRequest param = getTestRegistParam();
        MultipartFile testFile = new MockMultipartFile("file", "test.txt", "text/plain", "테스트 내용".getBytes(StandardCharsets.UTF_8));
        
        // when
        Long userNo = userService.regist(param, testFile);

        // then
        MyUser result = userService.getUser(userNo);
        assertNotNull(result);
    }
    
    @Test
    @DisplayName("회원가입 시 중복된 아이디인 경우")
    void failedDuplicatedId(){
        // given
        UserRegistRequest param1 = getTestRegistParam();
        UserRegistRequest param2 = getTestRegistParam();
        param2.setUserId("strawberry1234");
        userService.regist(param1, null);
        // when
        Exception e = Assertions.catchException(()-> userService.regist(param2, null));
        
        // then
        Assertions.assertThat(e).isInstanceOf(UserServiceException.class);
    }

    private UserRegistRequest getTestRegistParam() {
        UserRegistRequest param = new UserRegistRequest();
        param.setUserId("test");
        param.setUserEmail("test@test.com");
        param.setUserName("테스트");
        param.setUserPw("test");
        return param;
    }

    @Test
    @DisplayName("로그인 성공")
    void successLogin(){

    }
    @Test
    @DisplayName("잘못된 비밀번호로 로그인 실패")
    void failedPwNotMatched(){

    }
    @Test
    @DisplayName("비밀번호를 변경함")
    void successChangePw(){

    }
    @Test
    @DisplayName("비밀번호를 변경할 수 없음")
    void failedChangePw(){

    }
}
