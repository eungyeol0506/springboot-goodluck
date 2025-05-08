package com.example.goodluck.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.domain.UserRepository;
import com.example.goodluck.global.FilePathHelper;
import com.example.goodluck.global.SaveType;
import com.example.goodluck.global.ServiceExcepction;
import com.example.goodluck.service.LocalFileStorageService;
import com.example.goodluck.service.user.dto.UserEditRequest;
import com.example.goodluck.service.user.dto.UserLoginRequest;
import com.example.goodluck.service.user.dto.UserPwChangeRequest;
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
    public Long regist(UserRegistRequest param, MultipartFile image){
        MyUser user = userDataConvertor.toDomain(param);
        validateDuplicateUserId(user.getUserId());
        
        if(image != null){
            String relativeFileName = fileService.save(image, 0L, SaveType.PROFILE);
            if( !relativeFileName.equals("FAILED")){
                String path = FilePathHelper.getDirectoryPath(relativeFileName);
                String name = FilePathHelper.getFileNameOlny(path) + FilePathHelper.getExtension(path);
                user.setProfileImgPath(path);
                user.setProfileImgName(name);
            }
        }
        // db save
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));
        Long userNo = userRepository.save(user);
        
        return userNo;
        
    }

    /*
     * 로그인 메서드
     */
    public MyUser login(UserLoginRequest param){
        String id = param.getUsername();
        String pw = param.getPassword();

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
    public void update(UserEditRequest param, MultipartFile image) {

        MyUser user = userDataConvertor.toDomain(param);
        
        if(image != null){
            String relativeFileName = fileService.save(image, 0L, SaveType.PROFILE);
            if( !relativeFileName.equals("FAILED")){
                String path = FilePathHelper.getDirectoryPath(relativeFileName);
                String name = FilePathHelper.getFileNameOlny(path) + FilePathHelper.getExtension(path);
                user.setProfileImgPath(path);
                user.setProfileImgName(name);
            }
        }

        userRepository.update(user);
    }

    /*
     * 비밀번호 변경 메서드
     */
    public void changePw(Long userNo, UserPwChangeRequest param){
        String oldPw = param.getOldPw();
        String newPw = param.getNewPw();

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
        userRepository.updatePw(userNo, encodedPw);
    }
    
    
    private void validateDuplicateUserId(String userId) {
        userRepository.findById(userId).ifPresent(
            u -> { throw new UserServiceException(UserError.USER_ID_DUPLICATED, u);}
        );
    }
}
