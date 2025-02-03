package com.example.goodluck.myuser;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.goodluck.common.MyFileHandler;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.exception.myuser.UserLoginFaildException;
import com.example.goodluck.exception.myuser.UserNotFoundException;
import com.example.goodluck.myuser.dto.EditUserRequestDto;
import com.example.goodluck.myuser.dto.RegistUserRequestDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UserController {
    
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private MyFileHandler fileHandler;
    // @GetMapping(value = "/user/list", produces = MediaType.APPLICATION_JSON_VALUE)
    // @ResponseBody
    // public List<MyUser> getAllUsers(){
    //     return userService.getAllUserInfo();
    // }
    // @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    // @ResponseBody
    // public MyUser getUsers(@RequestParam(value="no") Long no) {
    //     return userService.getUserInfo(no);
    // }
    
    // 로그인
    @GetMapping("/login")
    public String getLoginView(Model model) {
        return "myuser/login";
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam(value="userId") String id,
                            @RequestParam(value="userPw") String pw, 
                            HttpServletRequest request ) {

        MyUser loginUser = userService.loginUser(id, pw).orElseThrow(()->new UserLoginFaildException("올바르지 않은 로그인 정보입니다."));
        // set session
        HttpSession session = request.getSession();
        session.setAttribute("userNo", loginUser.getUserNo());
        
        return "home";
    }
    // 로그아웃
    @GetMapping("/logout")
    public String getLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/";
    }
    
    // 회원가입
    @GetMapping("/regist")
    public String getRegisterView(Model model){
        model.addAttribute("preValue", new RegistUserRequestDto());
        return "myuser/regist_form";
    }
    
    @SuppressWarnings("null")
    @PostMapping("/regist")
    public String postRegister(
            @ModelAttribute(name="userRegistRequest") @Valid RegistUserRequestDto userRegistRequest,
            @RequestParam(value = "fileImage", required = false) MultipartFile multipartFile,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
        ){
        // dto -> domain
        MyUser user = userRegistRequest.toDomain(); 
        
        // save file
        if ( multipartFile != null && !multipartFile.getOriginalFilename().isBlank()){
            // MyFileHandler fileHandler = new MyFileHandler();
            String fileName = fileHandler.uploadMyUserProfileImage(multipartFile, user);
                                        
            user.setProfileImgName(fileName);
            user.setProfileImgPath(MyFileHandler.PROFILE_DIR_STRING);
        }
        // save DB data
        userService.registUser(user);
        
        redirectAttributes.addFlashAttribute("message", "회원가입에 성공하였습니다.");
        return "redirect:/login";   
    }

    //회원 정보 보기
    @GetMapping("/mypage")
    public String getUserInfo(HttpSession session, Model model ){
        Long userNo = (Long) session.getAttribute("userNo");
        MyUser resultUser = userService.getUserInfo(userNo).orElseThrow(
            () -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );

        model.addAttribute("user", resultUser);
        return "myuser/mypage" ;
    }

    //회원 정보 수정
    @GetMapping("/mypage/edit")
    public String getUserEditView(HttpSession session, Model model) { 
        Long userNo = (Long) session.getAttribute("userNo");
        MyUser resultUser = userService.getUserInfo(userNo).orElseThrow(
            () -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );

        model.addAttribute("preValue", resultUser);
        return "myuser/mypage_form" ;
    }

    @SuppressWarnings("null")
    @PostMapping("/mypage/edit")
    public String postUserEdit( 
            @ModelAttribute(name="userEditRequest") @Valid EditUserRequestDto userEditRequest,
            @RequestParam(value="fileImage", required=false) MultipartFile multipartFile,
            RedirectAttributes redirectAttributes) 
    {
        // dto -> domain
        MyUser user = userEditRequest.toDomain();
        
        // 이미지 업로드 하면 DB 데이터도 업뎃 name이 not blank
        // 이미지 업로드 안하면 DB 데이터는 있지만 name이 blank
        // 이미지 삭제를 원하면 name, path, blank 해야함
        
        // save file
        if ( multipartFile != null && !multipartFile.getOriginalFilename().isBlank()){
            String fileName = fileHandler.uploadMyUserProfileImage(multipartFile, user);
                                        
            user.setProfileImgName(fileName);
            user.setProfileImgPath(MyFileHandler.PROFILE_DIR_STRING);
        }        
        // save DB data
        userService.updateUser(user);
        
        redirectAttributes.addFlashAttribute("notice", "회원정보를 수정하였습니다.");
        return "redirect:/mypage";   
    }
    
}

    
