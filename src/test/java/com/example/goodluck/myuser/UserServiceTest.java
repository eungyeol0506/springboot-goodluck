package com.example.goodluck.myuser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.exception.myuser.UserLoginFaildException;
import com.example.goodluck.exception.myuser.UserPwNotMatchedException;
import com.example.goodluck.exception.myuser.UserRegistFaildException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock                                                                                                                                                                                                                                                                                                                                                        
    JdbcTemplateUserRepository mockUserRepository;

    //given 공통
    MyUser myUser = new MyUser();

    @BeforeEach
    void setUp(){
        myUser = new MyUser();
        myUser.setUserNo(Long.valueOf(999999));
        myUser.setUserId("test");
        myUser.setUserPw("test1234@");
        myUser.setUserName("테스트 데이터");
        myUser.setUserEmail("test@test.com");
    }

    @Nested
    @DisplayName("회원 로그인")
    class LoginUser{
        @Test
        @DisplayName("로그인을 성공한 경우")
        void successLoginUser(){
            // given
            MyUser myUser = MyUser.creatDummy(1L); // 테스트용 더미데이터
            String id = myUser.getUserId();
            String pw = myUser.getUserPw();
            BDDMockito.given(mockUserRepository.selectByIdPw(anyString(), anyString()))
                        .willReturn(Optional.of(myUser)); // 결과값을 반환하는 경우
            // when 
            MyUser resultUser = userService.loginUser(id, pw);
            // then
            Assertions.assertThat(resultUser).isInstanceOf(MyUser.class);
            Assertions.assertThat(myUser.getUserNo()).isEqualTo(resultUser.getUserNo());
            Assertions.assertThat(id).isEqualTo(resultUser.getUserId());
            Assertions.assertThat(pw).isEqualTo(resultUser.getUserPw());

            Mockito.verify(mockUserRepository).selectByIdPw(id, pw);
        }

        @DisplayName("ID, PW에 대해 회원정보가 없는 경우")
        @Test
        void failLoginUser(){
            //given
            String id = myUser.getUserId();
            String pw = myUser.getUserPw();
            Mockito.when(mockUserRepository.selectByIdPw(id, pw)).thenReturn(Optional.empty());
            //when 
            Exception exception = Assertions.catchException(() -> userService.loginUser(id, pw));
            //then
            Assertions.assertThat(exception)
                    .isInstanceOf(UserLoginFaildException.class)
                    .hasMessageContaining("올바르지 않은 로그인 정보입니다.");

            Mockito.verify(mockUserRepository).selectByIdPw(id,pw);
        }  

        @DisplayName("DB select 시 오류가 나는 경우")
        @Test
        void failDBAccessException(){
            // given
            String id = myUser.getUserId();
            String pw = myUser.getUserPw();
            Mockito.when(mockUserRepository.selectByIdPw(id, pw)).thenThrow(new DataAccessException("DB 예외 발생") {});
            // when
            Throwable exception = Assertions.catchThrowable(() -> mockUserRepository.selectByIdPw(id, pw));
            // then
            Assertions.assertThat(exception)
                    .isInstanceOf(DataAccessException.class)
                    .hasMessageContaining("DB 예외 발생");
            
            Mockito.verify(mockUserRepository).selectByIdPw(id, pw);
        }
    }

    @Nested
    @DisplayName("회원 가입")
    class RegistUser{

        @Nested
        class SuccessCase{
            @DisplayName("회원가입을 성공한 경우")
            @Test
            void successRegistUser(){
                // given
                String id = myUser.getUserId();
                BDDMockito.given(mockUserRepository.selectById(id)).willReturn(Optional.empty());
                BDDMockito.given(mockUserRepository.insertNew(myUser)).willReturn(myUser);
                // when
                MyUser resultUser = userService.registUser(myUser);
                // then
                Assertions.assertThat(myUser).isEqualTo(resultUser);

                Mockito.verify(mockUserRepository).selectById(id);
                Mockito.verify(mockUserRepository).insertNew(myUser);
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase{
            @Test
            @DisplayName("가입하려는 아이디가 중복인 경우")
            void failDuplicateUserId(){
                // given
                String id = myUser.getUserId();
                BDDMockito.given(mockUserRepository.selectById(id)).willReturn(Optional.of(myUser));
                // BDDMockito.given(mockUserRepository.insertNew(myUser)).willReturn(myUser); // stubbing 필요가 없음
                // when
                Exception exception = Assertions.catchException(() -> userService.registUser(myUser));
                // then
                Assertions.assertThat(exception)
                            .isInstanceOf(UserRegistFaildException.class)
                            .hasMessageContaining("존재하는 아이디");
                
                Mockito.verify(mockUserRepository).selectById(id);
            }
            
            @DisplayName("다른 예외가 발생한 경우")
            @Test
            void failElseException(){
                // given
                BDDMockito.given(mockUserRepository.selectById(anyString())).willThrow(new DataAccessException("Exception") {});
                // when
                Exception exception = Assertions.catchException(() -> userService.registUser(myUser));
                // then
                Assertions.assertThat(exception)
                        .isInstanceOf(DataAccessException.class)
                        .hasMessageContaining("Exception");
                
                Mockito.verify(mockUserRepository).selectById(anyString());
                Mockito.verify(mockUserRepository, Mockito.never()).insertNew(myUser);
                
                // // given
                // BDDMockito.given(mockUserRepository.insertNew(any(MyUser.class))).willThrow(new DataAccessException("Exception") {});
                // // when
                // Exception exception2 = Assertions.catchException(() -> userService.registUser(myUser));
                // // then
                // Assertions.assertThat(exception2)
                //             .isInstanceOf(DataAccessException.class)
                //             .hasMessageContaining("Exception");
                
                // Mockito.verify(mockUserRepository).selectById(anyString());
                // Mockito.verify(mockUserRepository).insertNew(myUser);
            }
        }
    }
    
    @Nested
    @DisplayName("회원정보 수정")
    class UpdateUser{
        @Nested
        class SuccessCase{
            @Test
            @DisplayName("회원 정보 수정에 성공한 경우")
            void successUpdateUser(){
                //given
                Long no = myUser.getUserNo();
                Mockito.when(mockUserRepository.updateUser(myUser)).thenReturn(Optional.of(myUser));
                //when
                MyUser resultUser = userService.updateUser(myUser).get();
                //then
                Mockito.verify(mockUserRepository).updateUser(myUser);

                Assertions.assertThat(myUser).isEqualTo(resultUser);
            }
        }
        @Nested
        @DisplayName("실패 케이스")
        class FailCase{
            @Test
            @DisplayName("존재하지 않는 회원정보인 경우")
            void failUserNotFound(){
                //given
                // Long no = myUser.getUserNo();
                Mockito.when(mockUserRepository.updateUser(myUser)).thenReturn(Optional.of(myUser));
                //when
                // Exception exception = Assertions.catchException(() -> userService.updateUser(myUser));
                MyUser result = userService.updateUser(myUser).get();
                //then
                Assertions.assertThat(result).isEqualTo(myUser);
                // Assertions.assertThat(exception)
                //         .isInstanceOf(UserNotFoundException.class)
                //         .hasMessageContaining("정보를 찾을 수 없습니다.");
                
                Mockito.verify(mockUserRepository).updateUser(myUser);
                // Mockito.verify(mockUserRepository, Mockito.never()).updateUser(myUser);
            }
            
            
            @Test
            @DisplayName("update시 exception이 발생한 경우")
            void failUpdatException(){
                // given
                Long no = myUser.getUserNo();
                // Mockito.when(mockUserRepository.selectByNo(no)).thenReturn(Optional.of(myUser));
                Mockito.when(mockUserRepository.updateUser(myUser)).thenThrow(new DataAccessException("update"){});
                // when
                Exception exception = Assertions.catchException(() -> userService.updateUser(myUser));
                // then
                Assertions.assertThat(exception)
                        .isInstanceOf(DataAccessException.class)
                        .hasMessageContaining("update");

                // Mockito.verify(mockUserRepository).selectByNo(no);
                Mockito.verify(mockUserRepository).updateUser(myUser);
            }
        }
    }

    @Nested
    @DisplayName("회원정보 조회하기")
    class GetUserInfo{
        @DisplayName("정보 조회가 성공한 경우")
        @Test
        void successGetUserInfo(){
            // given
            Long no = myUser.getUserNo();
            Mockito.when(mockUserRepository.selectByNo(no)).thenReturn(Optional.of(myUser));
            // when
            MyUser resultUser = userService.getUserInfo(no).get();
            // then
            Assertions.assertThat(myUser).isEqualTo(resultUser);
            Mockito.verify(mockUserRepository).selectByNo(no);
        }
        
        @DisplayName("회원정보가 없는 경우")
        @Test
        void failUserNotFound(){
            // given
            Long no = myUser.getUserNo();
            Mockito.when(mockUserRepository.selectByNo(no)).thenReturn(Optional.empty());
            // when
            Exception exception = Assertions.catchException(() -> userService.getUserInfo(no).get());
            // then
            Assertions.assertThat(exception)
                    .isInstanceOf(NoSuchElementException.class)
                    // .hasMessageContaining("정보를 찾을 수 없습니다.")
                    ;
            Mockito.verify(mockUserRepository).selectByNo(no);
        }

        @DisplayName("그 외 예외가 발생한 경우")
        @Test
        void failException(){
            // given
            Long no = myUser.getUserNo();
            Mockito.when(mockUserRepository.selectByNo(no)).thenThrow(new RuntimeException("new exception"));
            // when
            Exception exception = Assertions.catchException(() -> userService.getUserInfo(no));
            // then
            Assertions.assertThat(exception)
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("new exception");
                    
            Mockito.verify(mockUserRepository).selectByNo(no);
        }
    }
    

    @Nested
    @DisplayName("회원 비밀번호 변경 기능 테스트")
    class ChangeUserPw{

        @Nested
        class SuccessCase{
            @Test
            @DisplayName("비밀번호 변경에 성공한 경우")
            void successUpdateUserPw(){
                // given
                MyUser testUser = MyUser.creatDummy(0L);
                String newPassword = "new Password";

                BDDMockito.given(mockUserRepository.updateUser(any(MyUser.class))).willReturn(Optional.of(testUser));

                // when
                userService.updateUserPw(testUser, testUser.getUserPw(), newPassword);

                // then
                Assertions.assertThat(newPassword).isEqualTo(testUser.getUserPw());
                Mockito.verify(mockUserRepository).updateUser(any(MyUser.class));

            }
        }

        @Nested
        class failCase{
            @Test
            @DisplayName("새 비밀번호가 조건을 통과 못 하는 경우")
            void failDuplicatedUserPw(){
                // given
                MyUser testUser = MyUser.creatDummy(0L);
                String newPassword = "testtest"; // 동일한 비밀번호
                String oldPassword = "not same";

                // when
                Throwable exception1 = Assertions.catchThrowable(() -> userService.updateUserPw(testUser, testUser.getUserPw(), newPassword));
                Throwable exception2 = Assertions.catchThrowable(() -> userService.updateUserPw(testUser, oldPassword, newPassword));
                
                // then
                Assertions.assertThat(exception1).isInstanceOf(UserPwNotMatchedException.class);
                Assertions.assertThat(exception2).isInstanceOf(UserPwNotMatchedException.class);
                
                Assertions.assertThat(exception1).hasMessageContaining("동일합니다.");
                Assertions.assertThat(exception2).hasMessageContaining("동일하지 않습니다.");
                

                Mockito.verify(mockUserRepository, never()).updateUser(any(MyUser.class));
            }

            @Test
            @DisplayName("UserRepository에서 예외가 발생한 경우")
            void failElseException(){
                // given
                MyUser testUser = MyUser.creatDummy(0L);
                String newPassword = "new Password";

                BDDMockito.given(mockUserRepository.updateUser(any(MyUser.class))).willReturn(Optional.of(testUser));

                // when
                userService.updateUserPw(testUser, testUser.getUserPw(), newPassword);

                // then
                Assertions.assertThat(newPassword).isEqualTo(testUser.getUserPw());
                Mockito.verify(mockUserRepository).updateUser(any(MyUser.class));
            }
        }
    }

}
