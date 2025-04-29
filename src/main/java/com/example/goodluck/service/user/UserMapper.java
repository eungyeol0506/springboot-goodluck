package com.example.goodluck.service.user;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.service.user.in.UserEditParam;
import com.example.goodluck.service.user.in.UserRegisterParam;

public class UserMapper {
 
    public static MyUser toDomain(UserRegisterParam param){
        return MyUser.builder()
                    .userId(param.getUserId())
                    .userPw(param.getUserPw())
                    .userEmail(param.getUserEmail())
                    .addressMain(param.getAddressMain())
                    .addressDetail(param.getAddressDetail())
                    .telNo(param.getTelNo())
                    .postNo(param.getPostNo())
                    .build();
    }

    public static MyUser toDomain(UserEditParam param){
        return MyUser.builder()
                    .userNo(param.getUserNo())
                    .userId(param.getUserId())
                    .userPw(param.getUserPw())
                    .userEmail(param.getUserEmail())
                    .telNo(param.getTelNo())
                    .postNo(param.getPostNo())
                    .addressMain(param.getAddressMain())
                    .addressDetail(param.getAddressDetail())
                    .profileImgPath(param.getProfileImgPath())
                    .profileImgName(param.getProfileImgName())
                    .build();
    }
}
