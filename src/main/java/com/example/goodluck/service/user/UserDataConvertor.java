package com.example.goodluck.service.user;

import org.springframework.stereotype.Service;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.service.user.dto.UserEditRequest;
import com.example.goodluck.service.user.dto.UserRegistRequest;

@Service
public class UserDataConvertor {
 
    public MyUser toDomain(UserRegistRequest param){
        return MyUser.builder()
                    .userId(param.getUserId())
                    .userPw(param.getUserPw())
                    .userName(param.getUserName())
                    .userEmail(param.getUserEmail())
                    .addressMain(param.getAddressMain())
                    .addressDetail(param.getAddressDetail())
                    .telNo(param.getTelNo())
                    .postNo(param.getPostNo())
                    .build();
    }

    public MyUser toDomain(UserEditRequest param){
        return MyUser.builder()
                    .userName(param.getUserName())
                    .userEmail(param.getUserEmail())
                    .telNo(param.getTelNo())
                    .postNo(param.getPostNo())
                    .addressMain(param.getAddressMain())
                    .addressDetail(param.getAddressDetail())
                    // .profileImgPath(param.getProfileImgPath())
                    // .profileImgName(param.getProfileImgName())
                    .build();
    }
}
