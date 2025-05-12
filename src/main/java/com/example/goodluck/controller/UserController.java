package com.example.goodluck.controller;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.domain.AuthUser;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.global.helper.LoginSessionHelper;
import com.example.goodluck.service.user.UserService;
import com.example.goodluck.service.user.dto.UserEditRequest;
import com.example.goodluck.service.user.dto.UserLoginRequest;
import com.example.goodluck.service.user.dto.UserPwChangeRequest;
import com.example.goodluck.service.user.dto.UserRegistRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;

/*
 * 사용자 처리 컨트롤러
 * POST 매핑 시 성공은 redirect 처리해야 함
 */
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

        HttpSession session = request.getSession();

        // 삭제할 구문
        session.setAttribute("userNo", user.getUserNo());

        // 보안 로직
        AuthUser authUser = new AuthUser(user);

        // 인증 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            authUser,
            null,
            authUser.getAuthorities()
        );

        // SecurityContext에 등록
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                             context);

        return "redirect:/"; 
    }

    /*
     * 로그아웃 처리
     */
    @GetMapping("/logout")
    public String getLogout(HttpServletRequest request) {
        // SecurityContext 초기화
        SecurityContextHolder.clearContext();

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
            return "redirect:/";
    }


    /*
     * 회원 정보 조회
     */
    @GetMapping("/profile")
    public String getUserInfo(Model model){
        if( !LoginSessionHelper.isValidate()) return "redirect:/" ;        
        Long userNo = LoginSessionHelper.getUserNo();
        
        MyUser result = userService.getUser(userNo);

        model.addAttribute("user", result);
        return "user/profile" ;
    }

    /* 
     * 회원 정보 수정 처리
     */
    @GetMapping("/profile/form")
    public String getUserEditView(Model model) { 
        if( !LoginSessionHelper.isValidate()) return "redirect:/" ;        
        Long userNo = LoginSessionHelper.getUserNo();

        MyUser result = userService.getUser(userNo);
        UserEditRequest dto = convertDomainToDto(result);

        model.addAttribute("notice", "");
        model.addAttribute("requestData", dto);
        return "user/edit" ;
    }
    
    @PostMapping("/profile/form")
    public String postUserEdit( 
            @ModelAttribute(name="requestData") @Valid UserEditRequest param,
            @RequestParam(value="fileImage", required=false) MultipartFile file) 
    {
        if( !LoginSessionHelper.isValidate()) return "redirect:/" ;
        Long userNo = LoginSessionHelper.getUserNo();
        
        /* 강제로 요청 param에 setting */
        param.setUserNo(userNo);

        userService.update(param, file);
        return "redirect:/profle";   
    }
    
    /*
    * 비밀번호 변경 처리
    */
    @GetMapping("/password-change")
    public String getChangePasswordForm(Model model) {
        if( !LoginSessionHelper.isValidate()) return "redirect:/" ;
        model.addAttribute("requestData", new UserPwChangeRequest());
        return "user/password-change";
    }
    
    @PostMapping("/password-change")
    public String postChangePasswordForm(
        @ModelAttribute(name="requestData") @Valid UserPwChangeRequest param) 
    {
        if( !LoginSessionHelper.isValidate()) return "redirect:/" ;
        Long userNo = LoginSessionHelper.getUserNo();

        userService.changePw(userNo, param);

        return "redirect:/login";
            
    }

    private UserEditRequest convertDomainToDto(MyUser result) {
        UserEditRequest dto = new UserEditRequest();
        dto.setUserNo(result.getUserNo());
        dto.setUserName(result.getUserName());
        dto.setUserEmail(result.getUserEmail());
        dto.setPostNo(result.getPostNo());
        dto.setTelNo(result.getTelNo());
        dto.setAddressMain(result.getAddressMain());
        dto.setAddressDetail(result.getAddressDetail());
        dto.setProfileImgName(result.getProfileImgName());
        dto.setProfileImgPath(result.getProfileImgPath());
        return dto;
    }
        
}
    
    
    