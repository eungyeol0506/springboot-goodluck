package com.example.goodluck.controller;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.service.user.UserService;
import com.example.goodluck.service.user.dto.UserEditRequest;
import com.example.goodluck.service.user.dto.UserLoginRequest;
import com.example.goodluck.service.user.dto.UserRegistRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
     * 로그인 처리 
     */
    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("loginRequest", new UserLoginRequest());
        return "user/login";
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute(value = "loginRequest") @Valid UserLoginRequest param,
                            HttpServletRequest request ) {

        MyUser user = userService.login(param);

        // 인증 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user.getUserNo(),
            null
        );

        // SecurityContext에 등록
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        
        // set session
        HttpSession session = request.getSession();
        session.setAttribute("userNo", user.getUserNo());
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return "redirect:/"; 
    }

    /*
     * 로그아웃 처리
     */
    @GetMapping("/logout")
    public String getLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();

        return "home";
    }
    
    /*
     * 회원가입 처리
     */
    @GetMapping("/regist")
    public String getRegist(Model model){
        model.addAttribute("notice", "");
        model.addAttribute("requestData", new UserRegistRequest());
        return "user/regist";
    }
    
    @PostMapping("/regist")
    public String postRegister(
            @ModelAttribute(value ="requestData") @Valid UserRegistRequest param,
            @RequestParam(value = "fileImage", required = false) MultipartFile file
        ){
            userService.regist(param, file);
            return "home";
    }


    /*
     * 회원 정보 조회
     */
    @GetMapping("/profile")
    public String getUserInfo(HttpSession session, Model model ){
        Long userNo = (Long) session.getAttribute("userNo");
        MyUser result = userService.getUser(userNo);

        model.addAttribute("user", result);
        return "user/profile" ;
    }

    /* 
     * 회원 정보 수정 처리
     */
    @GetMapping("/profile/form")
    public String getUserEditView(HttpSession session, Model model) { 
        Long userNo = (Long) session.getAttribute("userNo");
        MyUser result = userService.getUser(userNo);

        UserEditRequest dto = new UserEditRequest();
        dto.setUserNo(userNo);
        dto.setUserName(result.getUserName());
        dto.setUserEmail(result.getUserEmail());
        dto.setPostNo(result.getPostNo());
        dto.setTelNo(result.getTelNo());
        dto.setAddressMain(result.getAddressMain());
        dto.setAddressDetail(result.getAddressDetail());
        dto.setProfileImgName(result.getProfileImgName());
        dto.setProfileImgPath(result.getProfileImgPath());

        model.addAttribute("notice", "");
        model.addAttribute("requestData", dto);
        return "user/edit" ;
    }

    @PostMapping("/profile/form")
    public String postUserEdit( 
            @ModelAttribute(name="requestData") @Valid UserEditRequest param,
            @RequestParam(value="fileImage", required=false) MultipartFile file) 
    {
        userService.update(param, file);
        return "redirect:/profle";   
    }
    
    /*
     * 비밀번호 변경 처리
     */
    // @GetMapping("/mypage/change-password")
    // public String getChangePasswordForm( HttpSession session ) {
    //     if(session.getAttribute("userNo") == null){
    //         return "redirect:/";
    //     }
    //     return "myuser/changePw_form";
    // }
    
    // @PostMapping("/mypage/change-password")
    // public String postChangePasswordForm(
    //     @ModelAttribute(name="changePasswordDto") @Valid UserPwChangeRequest changePasswordDto,
    //     HttpSession session) 
    // {
    //     Long userNo = (Long) session.getAttribute("userNo");
    //     if(userNo == null || userNo == 0){
    //         return "redirect:/";
    //     }
        
    //     MyUser findUser = userService.getUserInfo(userNo).orElseThrow(
    //                             () -> new UserNotFoundException("세션 사용자 정보를 찾을 수 없습니다.")
    //                         );
                            
    //     if (! changePasswordDto.isNotConfirmPasswordValue()){
    //         throw new UserPwNotMatchedException("비밀번호 확인값이 올바르지 않습니다.");
    //     }

    //     Map<String, String> inputPwValue = changePasswordDto.toDomain();
    //     String newPw = inputPwValue.get("newPw");
    //     String oldPw = inputPwValue.get("oldPw");

    //     userService.updateUserPw(findUser, oldPw, newPw);

    //     return "redirect:/mypage";
    // }
    

}

    
