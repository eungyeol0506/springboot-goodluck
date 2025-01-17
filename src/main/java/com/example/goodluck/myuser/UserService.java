package com.example.goodluck.myuser;

import java.util.List;
import java.util.Optional;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.exception.UserNotFoundException;
import com.example.goodluck.exception.UserNotFoundLoginException;


public class UserService {

    private UserRepository userRepository;
    // DI
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
    // 회원 가입
    public MyUser registUser(MyUser newMyUser){
        validateDuplicateUserId(newMyUser);
        return userRepository.insertNew(newMyUser);
    }
    private void validateDuplicateUserId(MyUser newMyUser) {
        userRepository.selectById(newMyUser.getUserId())
                    .ifPresent(u -> { 
                        throw new IllegalStateException("이미 존재하는 아이디입니다.");
                    });
    }
    // 회원 로그인
    public MyUser loginUser(String id, String pw){
        return userRepository.selectByIdPw(id, pw)
                             .orElseThrow(() -> new UserNotFoundLoginException("로그인 정보를 다시 확인해주세요."));
    }
    // 회원정보 수정
    public MyUser updateUser (MyUser editUser) {
        getUserInfo(editUser.getUserNo());
        return userRepository.updateUser(editUser)
                            .orElseThrow(() -> new IllegalStateException("회원 정보를 수정할 수 없습니다."));
    }
    // 회원정보 상세 보기
    public MyUser getUserInfo(Long no){
        return userRepository.selectByNo(no)
                             .orElseThrow(() -> new UserNotFoundException("정보를 찾을 수 없습니다."));
    }
    // 회원정보 전체 보기
    public List<MyUser> getAllUserInfo(){
        return userRepository.selectAll();
    } 
    // * ------------
    // 유저 정보를 찾는 경우
    public Optional<Long> findUserPw(String id, String email){
        return Optional.ofNullable(userRepository.selectByIdEmail(id, email)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자 정보입니다."))
                .getUserNo());
    }


    // 비밀번호만 변경하는 경우
    public MyUser updateUserPw(String userId, String newPw) throws RuntimeException{
        
        MyUser findUser = userRepository.selectById(userId)
                                        .orElseThrow( 
                                            () -> new IllegalStateException("회원 정보를 찾을 수 없습니다.")
                                        );
        if (findUser.getUserPw().equals(newPw)){
            throw new IllegalStateException("이전 비밀번호는 사용할 수 없습니다."); 
        }else if(newPw.isBlank()){
            throw new IllegalStateException("새 비밀번호는 공백일 수 없습니다."); 
        }
        findUser.setUserPw(newPw);
    
        return userRepository.updateUser(findUser)
                                .orElseThrow(() -> new IllegalStateException("비밀번호 변경에 실패했습니다."));
    }
    

}
