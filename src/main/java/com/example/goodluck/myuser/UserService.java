package com.example.goodluck.myuser;

import java.util.List;
import java.util.Optional;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.exception.myuser.UserLoginFaildException;
import com.example.goodluck.exception.myuser.UserNotFoundException;
import com.example.goodluck.exception.myuser.UserPwNotMatchedException;
import com.example.goodluck.exception.myuser.UserRegistFaildException;


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
                        throw new UserRegistFaildException("이미 존재하는 아이디입니다.", newMyUser);
                    });
    }
    // 회원 로그인
    public MyUser loginUser(String id, String pw){
        return userRepository.selectByIdPw(id, pw)
                             .orElseThrow(()->new UserLoginFaildException("올바르지 않은 로그인 정보입니다."));
    }
    // 회원정보 상세 보기
    public Optional<MyUser> getUserInfo(Long no){
        return userRepository.selectByNo(no);                            
    }
    // 회원정보 전체 보기
    public List<MyUser> getAllUserInfo(){
        return userRepository.selectAll();
    } 
    // 회원정보 수정
    public Optional<MyUser> updateUser (MyUser editUser) {
        if(editUser == null){
            throw new UserNotFoundException("존재하지 않는 수정정보입니다.");
        }
        return userRepository.updateUser(editUser);
    }
    
    //----
    // 유저 정보를 찾는 경우
    public Optional<Long> findUserPw(String id, String email){
        return Optional.ofNullable(userRepository.selectByIdEmail(id, email)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자 정보입니다."))
                .getUserNo());
    }

    // 비밀번호만 변경하는 경우
    public MyUser updateUserPw(MyUser user, String oldPw, String newPw) throws RuntimeException{
        // 이전 비밀번호가 사용자 비밀번호와 동일한지?
        if( ! user.getUserPw().equals(oldPw)){
            throw new UserPwNotMatchedException("기존 비밀번호 값이 동일하지 않습니다.");
        }
        // 이전 비밀번호와 새 비밀번호가 동일한지?
        if( oldPw.equals(newPw)){
            throw new UserPwNotMatchedException("새 비밀번호가 현재 비밀번호와 동일합니다.");
        }

        user.setUserPw(newPw);
    
        return userRepository.updateUser(user)
                             .orElseThrow(() -> new IllegalStateException("비밀번호 변경에 실패했습니다."));
    }
    

}
