package com.example.goodluck.myuser;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.goodluck.domain.MyUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class UserController {
    
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/user/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<MyUser> getAllUsers(){
        return userService.findAllUsers();
    }
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MyUser getUsers(@RequestParam(value="no") Long no) {
        return userService.getUserInfo(no).get();
    }
    
    // 로그인
    @GetMapping("/login")
    public String getLoginView(Model model) {
        return "myuser/login";
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam(value="userId") String id,
                            @RequestParam(value="userPw") String pw, 
                            HttpServletRequest request,
                            Model model) {
        try{
            MyUser loginUser = userService.loginUserService(id, pw).get();
            HttpSession session = request.getSession();
            session.setAttribute("userNo", loginUser.getUserNo());
            return "home";
        }catch(Exception e){
            model.addAttribute("message", e.getMessage());
            return "myuser/login";
        }
    }

    @GetMapping("/logout")
    public String getLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/";
    }
    
    // 회원가입
    @GetMapping("/regist")
    public String getRegisterView(){
        return "myuser/regist";
    }
    
    @PostMapping("/regist")
    public String postRegister(@RequestBody @Valid RegistUserRequestDto userDto,
                                BindingResult bindingResult,
                                Model model){
        // 검증 실패 처리
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDto); // 입력 값 유지
            model.addAttribute("message", bindingResult.getAllErrors().get(0).getDefaultMessage()); // 첫 번째 오류 메시지
            return "myuser/regist"; // 다시 등록 페이지로 이동
        }
        
        MyUser user = new MyUser();
        user.setUserName(userDto.getUserName());
        user.setUserId(userDto.getUserId());
        user.setUserPw(userDto.getUserPw());
        user.setUserEmail(userDto.getUserEmail());
        user.setPostNo(userDto.getPostNo());
        user.setAddressMain(userDto.getAddressMain());
        user.setAddressDetail(userDto.getAddressDetail());
        user.setTelNo(userDto.getTelNo());
        
        try{
            userService.registUser(user).get();
            return "redirect:/login";
        }catch(Exception e){
            model.addAttribute("user", user);
            model.addAttribute("message", e.getMessage());
            return "myuser/regist";
        }
    }

    //회원 정보 보기
    @GetMapping("/mypage")
    public String getUserInfo(HttpServletRequest request, 
                              Model model) {
        try{
            HttpSession session = request.getSession();
            // if(session == null){
            //     return "redirect:/login";
            // } 
            Long userNo = (Long) session.getAttribute("userNo");

            MyUser resultUser = userService.getUserInfo(userNo).get();   
            model.addAttribute("user", resultUser);    
            model.addAttribute("message", "안녕하세요");       
        }catch(Exception e){
            model.addAttribute("message", e.getMessage());
        }                     

        return "myuser/mypage" ;
    }

    //회원 정보 수정
    @GetMapping("/user/edit")
    public String getUserEditView(HttpServletRequest request,
                                 Model model) { 
        try{
            HttpSession session = request.getSession();
            Long userNo = (Long) session.getAttribute("userNo");

            MyUser resultUser = userService.getUserInfo(userNo).get();   
            model.addAttribute("user", resultUser);    
        }catch(Exception e){
            model.addAttribute("message", e.getMessage());
        }
        return "myuser/mypage_form" ;
    }

    @PostMapping("/user/edit")
    public String postUserEdit(){
        return "home";
    }
    
}

    
