package com.example.goodluck.global.exception;

import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.service.board.BoardServiceException;
import com.example.goodluck.service.user.UserServiceException;
import com.example.goodluck.service.user.dto.UserEditRequest;
import com.example.goodluck.service.user.dto.UserLoginRequest;
import com.example.goodluck.service.user.dto.UserRegistRequest;

import jakarta.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalExceptionHandler {
    /*
     * Business Logic 예외 처리
     */
    @ExceptionHandler(UserServiceException.class)
    public String handleUserServiceException(
        UserServiceException excepction,
        HttpServletRequest request,
        Model model
    ){
        MyUser user = excepction.getUser();
        String uri = request.getRequestURI();
        if(uri.contains("regist")){
            UserRegistRequest dto = new UserRegistRequest();
            dto.setUserId(user.getUserId());
            dto.setUserName(user.getUserName());
            dto.setUserEmail(user.getUserEmail());
            dto.setUserPw(user.getUserPw());
            dto.setAddressMain(user.getAddressMain());
            dto.setAddressDetail(user.getAddressDetail());
            dto.setPostNo(user.getPostNo());
            dto.setTelNo(user.getTelNo());
            
            model.addAttribute("registRequest", dto);
            // addDomainToModel(dto, model, request.getRequestURI());
        }else if(uri.contains("profile")){
            UserEditRequest dto = new UserEditRequest();
            dto.setUserEmail(user.getUserEmail());
            dto.setUserName(user.getUserName());
            dto.setTelNo(user.getTelNo());
            dto.setPostNo(user.getPostNo());
            dto.setAddressMain(user.getAddressMain());
            dto.setAddressDetail(user.getAddressDetail());
            // dto.setProfileImgName(user.getProfileImgName());
            // dto.setProfileImgPath(user.getProfileImgPath());
            dto.setImageFullPath(user.getImageResource());
            model.addAttribute("editRequest", dto);
        }
        model.addAttribute("notice", excepction.getErrorMessage());
        return ExceptionViewHelper.resolveViewNameByUri(request.getRequestURI(), model);
    }

    @ExceptionHandler(BoardServiceException.class)
    public String handleUserServiceException(
        BoardServiceException excepction,
        HttpServletRequest request,
        Model model
    ){
        /*
         * Board Error code 처리 로직 
         */
        
        return ExceptionViewHelper.resolveViewNameByUri(request.getRequestURI(), model);
    }

    /*
     * DTO validation 검증 실패 예외 처리 (이전 값을 setting 해주고, 해당 화면을 벗어나지 않음)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception, 
        HttpServletRequest request,
        Model model)
    {
        addBindingResultToModel(exception.getBindingResult(), model, request.getRequestURI());
    

        // String referer = request.getHeader("Referer");
        String requestUri = request.getRequestURI();
        return ExceptionViewHelper.resolveViewNameByUri(requestUri, model);
    }

    // 정의 외 에러 발생 
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException exception){

        return "error";
    }

    /*
     * 이전 값을 setting 해주기
     */

    private void addBindingResultToModel(BindingResult bindingResult, Model model, String uri){
        // 메시지 내용 수집
        String noticeMessage = bindingResult.getAllErrors()
                                .stream()
                                .map(ObjectError::getDefaultMessage)
                                .collect(Collectors.joining("\n"));

        model.addAttribute("notice", noticeMessage);

        // dto 내용을 다시 set해주기 
        ExceptionViewHelper.bindingTargetToModel(bindingResult, model, uri);
    }

    class ExceptionViewHelper{
        public static void bindingTargetToModel(BindingResult result, Model model, String uri){
            Object dto = result.getTarget();

            if (dto == null) return;
            
            if (uri.contains("regist")) {
                model.addAttribute("registRequest", dto);
            } else if (uri.contains("login")) {
                model.addAttribute("loginRequest", dto);
            } else if (uri.contains("profile")){
                model.addAttribute("editRequest", dto);
            } else if (uri.contains("password")){
                model.addAttribute("requestData", dto);
            } else if (uri.contains("write")){
                model.addAttribute("writeRequest", dto);
            } else if (uri.contains("modify")){
                model.addAttribute("modifyData", dto);
            } 

            // model.addAttribute("preValue", dto); // fallback
            
        }
        
        public static String resolveViewNameByUri(String requestUri, Model model){
            if(requestUri != null && requestUri.contains("regist")){
                return "user/regist";
            }
            else if(requestUri != null && requestUri.contains("login")){
                model.addAttribute("loginRequest", new UserLoginRequest());
                return "user/login";
            }
            else if(requestUri != null && requestUri.contains("profile")){
                return "user/edit";
            }
            else if(requestUri != null && requestUri.contains("password")){
                return "user/password-change";
            }
            else if(requestUri != null && requestUri.contains("write")){
                return "board/write";
            }
            else if(requestUri != null && requestUri.contains("modify")){
                return "board/modify";
            }
            return "error";
        }
    }
}
    