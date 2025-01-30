package com.example.goodluck.myuser;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.goodluck.common.MyFileHandler;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.exception.myuser.UserProfileImageUploadException;
import com.example.goodluck.exception.myuser.UserRegistFaildException;
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

        MyUser loginUser = userService.loginUser(id, pw);
        
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
        if ( multipartFile != null ){
            MyFileHandler fileHandler = new MyFileHandler();
            String fileName = fileHandler.uploadMyUserProfileImage(multipartFile, user).
                                        orElseThrow(() -> new UserRegistFaildException("이미지 업로드에 실패하였습니다."));
            user.setProfileImgName(fileName);
            if(!fileName.isEmpty()){
                user.setProfileImgPath(MyFileHandler.PROFILE_DIR_STRING);
            }
        }
        // save DB data
        userService.registUser(user);
        
        redirectAttributes.addFlashAttribute("message", "회원가입에 성공하였습니다.");
        return "redirect:/login";   
    }

    //회원 정보 보기
    @GetMapping("/mypage")
    public String getUserInfo(@SessionAttribute("userNo") Long userNo, Model model ){
        MyUser resultUser = userService.getUserInfo(userNo);

        if (resultUser.getProfileImgName() != null){
            model.addAttribute("profileUrl", resultUser.getProfile());
        }
        model.addAttribute("user", resultUser);
        return "myuser/mypage" ;
    }

    //회원 정보 수정
    @GetMapping("/mypage/edit")
    public String getUserEditView(@SessionAttribute("userNo") Long userNo, Model model) { 
        MyUser resultUser = userService.getUserInfo(userNo);

        model.addAttribute("user", resultUser);
        return "myuser/mypage_form" ;
    }

    @PostMapping("/mypage/edit")
    public String postUserEdit( 
            @ModelAttribute(name="userEditRequest") @Valid EditUserRequestDto userEditRequest,
            @RequestParam(value="fileImage", required=false) MultipartFile multipartFile,
            RedirectAttributes redirectAttributes,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            System.out.println(userEditRequest.getUserId());
        }
        // dto -> domain
        MyUser user = userEditRequest.toDomain();
        
        // save file
        String saveFileName =  user.getUserId();
        
        MyFileHandler fileHandler = new MyFileHandler();
        // if (multipartFile != null){
        //     String fileName = fileHandler.saveSingleFileFromMultipartFile(multipartFile, MyFileHandler.PROFILE_DIR, saveFileName).get();
        //     user.setProfileImgName(fileName);
        //     if(!fileName.isEmpty()){
        //         user.setProfileImgPath(MyFileHandler.PROFILE_DIR);
        //     }
        // }
        
        // save DB data
        userService.updateUser(user);
        
        redirectAttributes.addFlashAttribute("message", "회원정보를 수정하였습니다.");
        return "redirect:/mypage";   
    }
    
}

    
