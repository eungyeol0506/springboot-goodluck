package com.example.goodluck.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.domain.UserRepository;
import com.example.goodluck.global.SaveType;
import com.example.goodluck.global.ServiceExcepction;
import com.example.goodluck.service.LocalFileStorageService;
import com.example.goodluck.service.user.dto.UserRegistRequest;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDataConvertor userDataConvertor;
    private final LocalFileStorageService fileService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDataConvertor userDataConvertor, LocalFileStorageService fileService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDataConvertor = userDataConvertor;
        this.fileService = fileService;
    }
    
    /*
     * 회원가입 메서드
     */
    public MyUser regist(UserRegistRequest param, MultipartFile imageFile){
        // db save
        MyUser user = userDataConvertor.toDomain(param);
        validateDuplicateUserId(user);
        // 암호화
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));
        userRepository.save(user);
        
        // file save
        MyUser result = userRepository.findByNo(user.getUserNo()).get();
        String relativeFileName = fileService.save(imageFile, result.getUserNo(), SaveType.PROFILE);
        if( !relativeFileName.equals("FAILED")){
            // path, name, extension 추출
            // result.set
        }
        return result;
    }
    private void validateDuplicateUserId(MyUser user) {
        userRepository.findById(user.getUserId()).ifPresent(
            u -> { new UserServiceException(UserError.USER_ID_DUPLICATED);}
        );
    }

    /*
     * 로그인 메서드
     */
    public MyUser login(String id, String pw){

        MyUser user = userRepository.findById(id).orElseThrow(
            () -> new UserServiceException(UserError.USER_NOT_FOUND)
        );
        
        if(passwordEncoder.matches(pw, user.getUserPw())){
            return user;
        }

        throw new UserServiceException(UserError.USER_PW_NOT_MATCHED);
    }

    /*
     * 회원 정보 조회 메서드
     */
    public MyUser getUser(Long no){
        return userRepository.findByNo(no).orElseThrow(
            () -> new ServiceExcepction(UserError.USER_NOT_FOUND)
        );        

    }

    /*
     * 회원 정보 수정 메서드
     */
    public void update(MyUser user) {
        userRepository.update(user);
    }

    /*
     * 비밀번호 변경 메서드
     */
    public void changePw(Long userNo, String oldPw, String newPw){
        MyUser user = userRepository.findByNo(userNo).orElseThrow(
            ()-> new UserServiceException(UserError.USER_NOT_FOUND)
        );

        if(!passwordEncoder.matches(oldPw, user.getUserPw())){
            throw new UserServiceException(UserError.IS_NOT_USER_PW);
        }
        if(passwordEncoder.matches(newPw, user.getUserPw())){
            throw new UserServiceException(UserError.IS_SAME_PW);
        }

        String encodedPw = passwordEncoder.encode(newPw);
        user.setUserPw(encodedPw);

        userRepository.update(user);
    }
    

}
