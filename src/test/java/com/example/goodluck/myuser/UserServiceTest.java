package com.example.goodluck.myuser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.goodluck.domain.MyUser;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock                                                                                                                                                                                                                                                                                                                                                        
    JdbcTemplateUserRepository mockUserRepository;

    @Nested
    @DisplayName("로그인")
    class LoginUser{
        @Test
        @DisplayName("주어진 id, pw로 검색 시 User값이 있음 = 로그인 성공")
        void LoginSuccessCase(){
            //given
            MyUser testUser = getTestNewUser();

            //when 
            when(mockUserRepository.selectByIdPw(any(String.class), any(String.class))).thenReturn(Optional.of(testUser));
            UserService userService = new UserService(mockUserRepository);

            //then
            MyUser resultUser = userService.loginUserService(testUser.getUserId(), testUser.getUserPw()).get();
            Assertions.assertThat(resultUser.getUserNo()).isEqualTo(testUser.getUserNo());

        }
        @Test
        @DisplayName("주어진 id, pw로 검색 시 User값이 없음 = 로그인 실패")
        void LoginFailCase(){
            //given
            MyUser testUser = getTestNewUser();

            //when 
            when(mockUserRepository.selectByIdPw(any(String.class), any(String.class))).thenReturn(Optional.empty());
            UserService userService = new UserService(mockUserRepository);

            //then
            try {
                MyUser resultUser = userService.loginUserService(testUser.getUserId(), testUser.getUserPw()).get();
            }catch(Exception e){
                // Assertions.assertThat(resultUser.getUserNo()).isEqualTo(testUser.getUserNo());
                Assertions.assertThat(e.getMessage()).isEqualTo("로그인 정보를 다시 확인해주세요.");
            }

        }        
    }

    @Nested
    @DisplayName("회원 가입")
    class RegistUser{
        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase{
            @Test
            @DisplayName("새로운 회원 가입")
            void registUserSuccess(){
                MyUser newUser = getTestNewUser();

                when(mockUserRepository.insertNew(any(MyUser.class))).thenReturn(newUser);
                // when(userRepository.selectById(any(String.class))).thenReturn(Optional.empty());
                UserService userService = new UserService(mockUserRepository);
                MyUser resultUser = userService.registUser(newUser).get();

                Assertions.assertThat(resultUser.getUserNo()).isEqualTo(newUser.getUserNo());
            }
        }
        @Nested
        @DisplayName("실패 케이스")
        class FailCase{
            @Test
            @DisplayName("아이디가 중복인 경우")
            void registDuplicateUserId(){
                MyUser newUser = getTestNewUser();

                // when(userRepository.insertNew(any(MyUser.class))).thenReturn(newUser);
                when(mockUserRepository.selectById(any(String.class))).thenReturn(Optional.ofNullable(newUser));
                UserService userService = new UserService(mockUserRepository);
                try{
                    MyUser resultUser = userService.registUser(newUser).get();
                    Assertions.assertThat(resultUser.getUserNo()).isEqualTo(newUser.getUserNo());
                }catch(Exception e){
                    Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 아이디입니다.");
                }

            }
        }

        
    }
    
    @Nested
    @DisplayName("회원 정보 수정 CASE")
    class EditUserInfo{
        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase{
            @Test
            @DisplayName("회원 정보 수정 성공")
            void updateUserInfo(){
                //given
                MyUser editedUser = getTestNewUser();
                Mockito.when(mockUserRepository.updateUser(editedUser)).thenReturn(Optional.of(editedUser));
                //when
                MyUser resultUser = userService.updateUser(editedUser);
                //then
                Mockito.verify(mockUserRepository).updateUser(editedUser);
                Assertions.assertThat(editedUser).isEqualTo(resultUser);
            }
        }

        @Nested
        class FailCase{
            @Test
            @DisplayName("정보 수정 - DB에 없는 회원 정보인 경우")
            void userNotFound(){
                //given
                MyUser editedUser = getTestNewUser();
                Mockito.when(mockUserRepository.updateUser(editedUser)).thenReturn(Optional.empty());
                //when
                Throwable exception = Assertions.catchThrowable(() -> userService.updateUser(editedUser));
                //then
                Mockito.verify(mockUserRepository).updateUser(editedUser);
                Assertions.assertThat(exception)
                          .isInstanceOf(RuntimeException.class)
                          .hasMessageContaining("수정하려는 값을 찾을 수 없습니다.");
            }
            @Test
            @DisplayName("정보 수정 - DB update가 실패한 경우")
            void dbUpdateException(){
                // given
                MyUser editedUser = getTestNewUser();
                Mockito.when(mockUserRepository.updateUser(editedUser)).thenThrow(new RuntimeException("업데이트 에러"));
                // when
                Throwable exception = Assertions.catchThrowable(() -> userService.updateUser(editedUser));
                // tehn
                Mockito.verify(mockUserRepository).updateUser(editedUser);
                Assertions.assertThat(exception)
                          .isInstanceOf(RuntimeException.class)
                          .hasMessage("업데이트 에러");
            }
        }
    }
    
    @Nested
    @DisplayName("회원 비밀번호 변경")
    class ChangeUserPw{
        @Nested
        class SuccessCase{
            @Test
            @DisplayName("비밀 번호찾기로 비밀번호만 변경 성공")
            void updateUserPw(){
                // given - 깊은 복사를 못 해서 이렇게...
                MyUser user = getTestNewUser();
                MyUser editedUser = getTestNewUser();
                
                String newPw = "newPassword";
                editedUser.setUserPw("newPassword");

                Mockito.when(mockUserRepository.updateUser(any(MyUser.class))).thenReturn(Optional.of(editedUser));
                Mockito.when(mockUserRepository.selectById(user.getUserId())).thenReturn(Optional.of(user));
                // when
                MyUser resultUser = userService.updateUserPw(user.getUserId(), newPw);
                // then
                Assertions.assertThat(user.getUserId()).isEqualTo(resultUser.getUserId());
                Assertions.assertThat(newPw).isEqualTo(resultUser.getUserPw());
                Assertions.assertThat(user.getUserPw()).isNotEqualTo(resultUser.getUserPw());
                // verify
                Mockito.verify(mockUserRepository).updateUser(user);
                Mockito.verify(mockUserRepository).selectById(user.getUserId());
            }
        }
        @Nested
        class FailCase{
            @Test
            @DisplayName("비밀번호 변경 - 회원 정보가 없는 경우")
            void userNotFound(){

            }
            // @DisplayName("비밀번호 변경 - 이전 비밀번호와 동일한 경우")
            // @DisplayName("비밀번호 변경 - 비밀번호를 입력하지 않은 경우")
            // Mockito.verifyNoInteractions(mockUserRepository);

        }
    }

    @Nested
    @DisplayName("회원 정보 삭제")
    class DeleteUser{
        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase{
            @Test
            @DisplayName("회원 정보 삭제 성공")
            void deleteUser(){

            }
        }

        class FailCase{
            
        }
    }
    private MyUser getTestNewUser() {
        MyUser newUser = new MyUser();
        newUser.setUserNo(Long.valueOf(123456));
        newUser.setUserId("test");
        newUser.setUserPw("test");
        newUser.setUserName("테스터");
        return newUser;
    }
}
