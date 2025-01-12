package com.example.goodluck.myuser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.goodluck.common.MyFileHandler;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.exception.UserProfileImageUploadException;
import com.example.goodluck.myuser.dto.EditUserRequestDto;
import com.example.goodluck.myuser.dto.RegistUserRequestDto;

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
    // 사용자 이미지 프로필 파일 경로
    private static final String PROFILE_DIR = "src/main/resources/files";

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
    public String getRegisterView(){
        return "myuser/regist_form";
    }
    
    @PostMapping("/regist")
    public String postRegister(
            @ModelAttribute(name="userRegistRequest") @Valid RegistUserRequestDto userRegistRequest,
            @RequestParam(value = "fileImage", required = false) MultipartFile multipartFile,
            Model model,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
        ){
        // dto -> domain
        MyUser user = userRegistRequest.toDomain();
        
        // save file
        String saveFileName =  user.getUserId();
        
        MyFileHandler fileHandler = new MyFileHandler();
        if (multipartFile != null){
            String fileName = fileHandler.saveSingleFileFromMultipartFile(multipartFile, MyFileHandler.PROFILE_DIR, saveFileName).get();
            user.setProfileImgName(fileName);
            if(!fileName.isEmpty()){
                user.setProfileImgPath(MyFileHandler.PROFILE_DIR);
            }
        }
        
        // save DB data
        userService.registUser(user);
        
        // model.addAttribute("user", user);
        redirectAttributes.addFlashAttribute("message", "회원가입에 성공하였습니다.");
        return "redirect:/login";   
    }

    //회원 정보 보기
    @GetMapping("/mypage")
    public String getUserInfo(@SessionAttribute("userNo") Long userNo, Model model ){
        MyUser resultUser = userService.getUserInfo(userNo);

        model.addAttribute("user", resultUser);     
        return "myuser/mypage" ;
    }

    //회원 정보 수정
    @GetMapping("/user/edit")
    public String getUserEditView(HttpServletRequest request,
                                  Model model) { 
        try{
            HttpSession session = request.getSession();
            Long userNo = (Long) session.getAttribute("userNo");

            MyUser resultUser = userService.getUserInfo(userNo);   
            model.addAttribute("user", resultUser);    
        }catch(Exception e){
            model.addAttribute("message", e.getMessage());
        }
        return "myuser/mypage_form" ;
    }

    @PostMapping("/user/edit")
    public String postUserEdit( @ModelAttribute(name="userEditRequest") @Valid EditUserRequestDto userEditRequest,
                                @RequestParam("fileImage") MultipartFile multipartFile,
                                Model model,
                                BindingResult bindingResult,
                                HttpSession httpSession,
                                RedirectAttributes redirectAttributes) throws Exception{
                       
        try {
            // dto -> domain 
            MyUser user = userEditRequest.toDomain();
            String extension = "png";
            String savefileName = String.valueOf(user.getUserNo()) + "_" + user.getUserId() + "." + extension ;
            Path uploadPath = Paths.get(PROFILE_DIR);
            // create new dir
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = uploadPath.resolve(savefileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            // set DB user profile
            user.setProfileImgName(savefileName);
            user.setProfileImgPath(PROFILE_DIR);
            // MyUser updatedMyUser = userService.updateUser(user).get();
            userService.updateUser(user);
            
            return "redirect:/mypage";
        } catch (Exception exception) {
            model.addAttribute("user", userEditRequest.toDomain());
            model.addAttribute("message", exception.getMessage());
            return "myuser/mypage";
        }
        
    }
    
}

    
