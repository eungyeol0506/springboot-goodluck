// package com.example.goodluck.controller;


// import java.io.IOException;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import com.example.goodluck.domain.MyUser;
// import com.example.goodluck.global.MyUserProfileImageHandler;
// import com.example.goodluck.service.user.UserService;
// import com.example.goodluck.service.user.dto.UserEditRequest;
// import com.example.goodluck.service.user.dto.UserPwChangeRequest;
// import com.example.goodluck.service.user.dto.UserRegistRequest;

// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpSession;
// import jakarta.validation.Valid;

// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;



// @Controller
// public class UserController {
    
//     private final UserService userService;

//     public UserController(UserService userService) {
//         this.userService = userService;
//     }

//     // private MyFileHandler fileHandler;

//     // 로그인
//     @GetMapping("/login")
//     public String getLoginView(Model model) {
//         return "myuser/login";
//     }

//     @PostMapping("/login")
//     public String postLogin(@RequestParam(value="userId") String id,
//                             @RequestParam(value="userPw") String pw, 
//                             HttpServletRequest request ) {

//         MyUser loginUser = userService.loginUser(id, pw);
//         // set session
//         HttpSession session = request.getSession();
//         session.setAttribute("userNo", loginUser.getUserNo());
        
//         return "home";
//     }
//     // 로그아웃
//     @GetMapping("/logout")
//     public String getLogout(HttpServletRequest request) {
//         HttpSession session = request.getSession();
//         session.invalidate();

//         return "redirect:/";
//     }
    
//     // 회원가입
//     @GetMapping("/regist")
//     public String getRegisterView(Model model){
//         model.addAttribute("preValue", new UserRegistRequest());
//         return "myuser/regist_form";
//     }
    
//     @SuppressWarnings("null")
//     @PostMapping("/regist")
//     public String postRegister(
//             @ModelAttribute(name="userRegistRequest") @Valid UserRegistRequest userRegistRequest,
//             @RequestParam(value = "fileImage", required = false) MultipartFile multipartFile,
//             RedirectAttributes redirectAttributes
//         ){
//         // dto -> domain
//         MyUser user = userRegistRequest.toDomain(); 
        
//         // save file
//         saveProfileImage(multipartFile, user);
        
//         // save DB data
//         userService.registUser(user);
        
//         redirectAttributes.addFlashAttribute("message", "회원가입에 성공하였습니다.");
//         return "redirect:/login";   
//     }

//     private void saveProfileImage(MultipartFile multipartFile, MyUser user) {
//         if ( multipartFile != null && !multipartFile.getOriginalFilename().isBlank()){
//             MyUserProfileImageHandler fileHandler = MyUserProfileImageHandler.builder()
//                                                                             .user(user)
//                                                                             .build();
//             try{
//                 fileHandler.saveFile(multipartFile);
//                 // Test code
//                 // fileHandler.saveFileTest(multipartFile);
//             }catch(IOException exception){
//                 throw new UserProfileImageUploadException("프로필이미지 저장에 실패하였습니다.", user);
//             }
                                        
//             user.setProfileImgName(fileHandler.getFileName());
//             user.setProfileImgPath(fileHandler.getDirName());
//         }
//     }

//     // 회원 정보 보기
//     @GetMapping("/mypage")
//     public String getUserInfo(HttpSession session, Model model ){
//         Long userNo = (Long) session.getAttribute("userNo");
//         MyUser resultUser = userService.getUserInfo(userNo).orElseThrow(
//             () -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다.")
//         );

//         model.addAttribute("user", resultUser);
//         return "myuser/mypage" ;
//     }

//     //회원 정보 수정
//     @GetMapping("/mypage/edit")
//     public String getUserEditView(HttpSession session, Model model) { 
//         Long userNo = (Long) session.getAttribute("userNo");
//         MyUser resultUser = userService.getUserInfo(userNo).orElseThrow(
//             () -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다.")
//         );

//         model.addAttribute("preValue", resultUser);
//         return "myuser/mypage_form" ;
//     }

//     @PostMapping("/mypage/edit")
//     public String postUserEdit( 
//             @ModelAttribute(name="userEditRequest") @Valid UserEditRequest userEditRequest,
//             @RequestParam(value="fileImage", required=false) MultipartFile multipartFile,
//             RedirectAttributes redirectAttributes) 
//     {
//         // dto -> domain
//         MyUser user = userEditRequest.toDomain();
        
//         // save file
//         saveProfileImage(multipartFile, user);       
        
//         // save DB data
//         userService.updateUser(user);
        
//         redirectAttributes.addFlashAttribute("notice", "회원정보를 수정하였습니다.");
//         return "redirect:/mypage";   
//     }
    
//     @GetMapping("/mypage/change-password")
//     public String getChangePasswordForm( HttpSession session ) {
//         if(session.getAttribute("userNo") == null){
//             return "redirect:/";
//         }
//         return "myuser/changePw_form";
//     }
    
//     @PostMapping("/mypage/change-password")
//     public String postChangePasswordForm(
//         @ModelAttribute(name="changePasswordDto") @Valid UserPwChangeRequest changePasswordDto,
//         HttpSession session) 
//     {
//         Long userNo = (Long) session.getAttribute("userNo");
//         if(userNo == null || userNo == 0){
//             return "redirect:/";
//         }
        
//         MyUser findUser = userService.getUserInfo(userNo).orElseThrow(
//                                 () -> new UserNotFoundException("세션 사용자 정보를 찾을 수 없습니다.")
//                             );
                            
//         if (! changePasswordDto.isNotConfirmPasswordValue()){
//             throw new UserPwNotMatchedException("비밀번호 확인값이 올바르지 않습니다.");
//         }

//         Map<String, String> inputPwValue = changePasswordDto.toDomain();
//         String newPw = inputPwValue.get("newPw");
//         String oldPw = inputPwValue.get("oldPw");

//         userService.updateUserPw(findUser, oldPw, newPw);

//         return "redirect:/mypage";
//     }
    

// }

    
