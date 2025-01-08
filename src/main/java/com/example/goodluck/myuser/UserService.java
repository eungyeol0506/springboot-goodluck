package com.example.goodluck.myuser;

import java.util.List;
import java.util.Optional;

import com.example.goodluck.domain.MyUser;


public class UserService {
    
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public List<MyUser> findAllUsers(){
        return userRepository.selectAll();
    } 

    // 회원 가입
    public Optional<MyUser> registUser(MyUser newMyUser){
        validateDuplicateUserId(newMyUser);
        
        userRepository.insertNew(newMyUser);
        return Optional.ofNullable(newMyUser);
    }

    private void validateDuplicateUserId(MyUser newMyUser) {
        userRepository.selectById(newMyUser.getUserId())
                        .ifPresent(u -> { 
                            throw new IllegalStateException("이미 존재하는 아이디입니다.");
                        });
    }
    // 유저 정보 상세보기
    public Optional<MyUser> getUserInfo(Long no){
        return userRepository.selectByNo(no);
    }
    
    // 유저 로그인 - 알맞는 ID, PW 입력됨
    public Optional<MyUser> loginUserService(String id, String pw){
        
        MyUser user =  userRepository.selectByIdPw(id, pw)
                        .orElseThrow(() -> new IllegalStateException("로그인 정보를 다시 확인해주세요."));
        return Optional.ofNullable(user);
    }

    // 유저 정보를 찾는 경우
    public Optional<Long> findUserPw(String id, String email){
        return Optional.ofNullable(userRepository.selectByIdEmail(id, email)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자 정보입니다."))
                .getUserNo());
    }

    // 유저 정보 수정하는 경우
    public MyUser updateUser(MyUser user){
        try{
            return userRepository.updateUser(user)
                                 .orElseThrow(() -> new IllegalStateException("수정하려는 값을 찾을 수 없습니다."));
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    // 비밀번호만 변경하는 경우
    public MyUser updateUserPw(String userId, String newPw){
        MyUser findUser = userRepository.selectById(userId)
                                        .orElseThrow( 
                                            () -> new IllegalStateException("회원 정보를 찾을 수 없습니다.")
                                        );
        if (findUser.getUserPw() == newPw){
            throw new IllegalStateException("이전 비밀번호는 사용할 수 없습니다.");
        }else{
            findUser.setUserPw(newPw);
        }

        try{
            return userRepository.updateUser(findUser)
                                 .orElseThrow(() -> new IllegalStateException("비밀번호 변경에 실패했습니다."));
        } catch(Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
}
