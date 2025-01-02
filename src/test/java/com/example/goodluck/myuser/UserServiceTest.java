package com.example.goodluck.myuser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.goodluck.domain.MyUser;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    // @InjectMocks
    // private UserService userService;

    @Mock                                                                                                                                                                                                                                                                                                                                                        
    private JdbcTemplateUserRepository userRepository;

    @Nested
    @DisplayName("로그인")
    class LoginUser{
        @Test
        @DisplayName("주어진 id, pw로 검색 시 User값이 있음 = 로그인 성공")
        void LoginSuccessCase(){
            //given
            MyUser testUser = getTestNewUser();

            //when 
            when(userRepository.selectByIdPw(any(String.class), any(String.class))).thenReturn(Optional.of(testUser));
            UserService userService = new UserService(userRepository);

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
            when(userRepository.selectByIdPw(any(String.class), any(String.class))).thenReturn(Optional.empty());
            UserService userService = new UserService(userRepository);

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

                when(userRepository.insertNew(any(MyUser.class))).thenReturn(newUser);
                // when(userRepository.selectById(any(String.class))).thenReturn(Optional.empty());
                UserService userService = new UserService(userRepository);
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
                when(userRepository.selectById(any(String.class))).thenReturn(Optional.ofNullable(newUser));
                UserService userService = new UserService(userRepository);
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
    @DisplayName("회원 정보 수정")
    class UpdateUser{
        @Nested
        @DisplayName("성공 케이스")
        class SuccessCase{
            @Test
            @DisplayName("비밀 번호 수정 성공")
            void updatePw(){
                String newPw = "";
            }
            @Test
            @DisplayName("회원 정보 수정 성공")
            void updateUserInfo(){

            }
        }

        class FailCase{

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
