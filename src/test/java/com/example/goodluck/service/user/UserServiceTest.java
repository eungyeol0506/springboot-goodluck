package com.example.goodluck.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.global.ServiceExcepction;
import com.example.goodluck.service.user.dto.UserLoginRequest;
import com.example.goodluck.service.user.dto.UserPwChangeRequest;
import com.example.goodluck.service.user.dto.UserRegistRequest;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired PasswordEncoder passwordEncoder;

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
        UserRegistRequest param = getTestRegistParam();
        param.setUserId("1234");
        // when
        Exception e = Assertions.catchException(()-> userService.regist(param, null));
        
        // then
        Assertions.assertThat(e).isInstanceOf(UserServiceException.class);
        ServiceExcepction excepction = (ServiceExcepction)e;
        assertEquals(UserError.USER_ID_DUPLICATED.getMessage(), excepction.getErrorMessage());
    }

    private UserRegistRequest getTestRegistParam() {
        UserRegistRequest param = new UserRegistRequest();
        param.setUserId("123456789");
        param.setUserEmail("test@test.com");
        param.setUserName("테스트");
        param.setUserPw("test");
        return param;
    }

    @Test
    @DisplayName("로그인 성공")
    void successLogin(){
        UserLoginRequest param = getTestLoginParam();

        MyUser result = userService.login(param);

        assertNotNull(result);
        assertEquals(param.getUserName(), result.getUserId());
    }
    
    @Test
    @DisplayName("잘못된 비밀번호로 로그인 실패")
    void failedPwNotMatched(){
        UserLoginRequest param = getTestLoginParam();
        param.setPassword("123456789");

        Exception e = Assertions.catchException(() -> userService.login(param));

        Assertions.assertThat(e).isInstanceOf(UserServiceException.class);
        ServiceExcepction excepction = (ServiceExcepction) e;
        assertEquals(UserError.USER_NOT_FOUND.getMessage(), excepction.getErrorMessage());
    }

    @Test
    @DisplayName("존재하지 않는 사용자여도 로그인 실패")
    void failedUserNotFound(){
        UserLoginRequest param = getTestLoginParam();
        param.setPassword("1234");

        Exception e = Assertions.catchException(() -> userService.login(param));

        Assertions.assertThat(e).isInstanceOf(UserServiceException.class);
        ServiceExcepction excepction = (ServiceExcepction) e;
        assertEquals(UserError.USER_PW_NOT_MATCHED.getMessage(), excepction.getErrorMessage());
    }

    private UserLoginRequest getTestLoginParam(){
        UserLoginRequest param = new UserLoginRequest("1234567", "test");

        return param;
    }

    @Test
    @DisplayName("비밀번호를 변경함")
    void successChangePw(){
        // given
        Long userNo = userService.regist(getTestRegistParam(), null);
        UserPwChangeRequest param = getTestChange();

        // when
        userService.changePw(userNo, param);

        // then
        MyUser result = userService.getUser(userNo);
        assertNotNull(result);
        assertEquals(userNo, result.getUserNo());
        assertTrue(passwordEncoder.matches(param.getNewPw(), result.getUserPw()));
    }

    @Test
    @DisplayName("비밀번호를 변경할 수 없음")
    void failedChangePw(){
        // given
        Long userNo = userService.regist(getTestRegistParam(), null);
        UserPwChangeRequest param = getTestChange();
        // 기존 비밀번호랑 같은 비밀번호
        // param.setNewPw(param.getOldPw());
        // 기존 비밀번호가 틀림
        param.setOldPw("not same");

        // when
        Exception e = Assertions.catchException(() -> userService.changePw(userNo, param));

        // then
        Assertions.assertThat(e).isInstanceOf(ServiceExcepction.class);
        ServiceExcepction excepction = (ServiceExcepction) e;
        // assertEquals(UserError.IS_SAME_PW.getMessage(), excepction.getErrorMessage());
        assertEquals(UserError.IS_NOT_USER_PW.getMessage(), excepction.getErrorMessage());

    }

    private UserPwChangeRequest getTestChange(){
        UserPwChangeRequest param = new UserPwChangeRequest();
        param.setOldPw("test");
        param.setNewPw("1234");
        return param;
    }
}
