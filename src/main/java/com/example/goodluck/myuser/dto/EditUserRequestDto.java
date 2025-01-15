package com.example.goodluck.myuser.dto;

import com.example.goodluck.common.MyDto;
import com.example.goodluck.domain.MyUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditUserRequestDto implements MyDto<MyUser>{
    // form 에서 작성하는 내용 
    // 변수명은 form 내 id 태그값
    @NotNull(message = "사용자 번호는 필수값입니다.")
    private Long userNo;
    @NotBlank(message = "사용자 이름은 필수값입니다.")
    private String userName;
    @NotBlank(message = "사용자 아이디는 필수값입니다.")
    private String userId;
    @Size(min=5, max=16, message = "비밀번호는 6글자 이상, 16글자를 초과할 수 없습니다.")
    private String userPw;
    @Email(message = "이메일 형식이 아닙니다.")
    private String userEmail;
    @Size(max=5, message="유효하지 않은 우편번호 형식입니다.")
    private String postNo;
    private String telNo;
    private String addressMain;
    private String addressDetail;

    // 도메인 별 특정 request가 존재하기 때문에 해당 변환 함수를 DTO 내 구현함
    public MyUser toDomain(){
        MyUser domain = new MyUser();
        domain.setUserNo(this.userNo);
        domain.setUserId(this.userId);
        domain.setUserPw(this.userPw);
        domain.setUserName(this.userName);
        domain.setUserEmail(this.userEmail);
        domain.setAddressMain(this.addressMain);
        domain.setAddressDetail(this.addressDetail);
        domain.setPostNo(this.postNo);
        domain.setTelNo(this.telNo);
        
        return domain;
    }
}
