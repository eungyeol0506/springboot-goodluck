package com.example.goodluck.myuser;

import java.util.List;
import java.util.Optional;

import com.example.goodluck.domain.MyUser;

public interface UserRepository {
    MyUser insertNew(MyUser newMyUser);    
    Optional<MyUser> selectByNo(Long no); 
    Optional<MyUser> selectById(String id);
    Optional<MyUser> selectByIdPw(String id, String pw);
    Optional<MyUser> selectByIdEmail(String id, String email);
    List<MyUser> selectAll();
    Optional<Long> deleteByNo(Long no);
    Optional<MyUser> updateUser(MyUser user);
}
