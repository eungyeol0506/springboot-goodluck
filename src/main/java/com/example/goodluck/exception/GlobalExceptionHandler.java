package com.example.goodluck.exception;

import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.goodluck.exception.myuser.UserLoginFaildException;
import com.example.goodluck.exception.myuser.UserNotFoundException;
import com.example.goodluck.exception.myuser.UserProfileImageUploadException;
import com.example.goodluck.exception.myuser.UserRegistFaildException;
import com.example.goodluck.myuser.dto.RegistUserRequestDto;

import jakarta.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalExceptionHandler {
    // MyUser 관련 예외
    @ExceptionHandler(UserRegistFaildException.class)
    public String handleUserRegistFaildException(
        UserRegistFaildException exception,
        Model model)
    {
        RegistUserRequestDto dto = exception.getRegistUserRequestDto();
        if(dto != null){
            model.addAttribute("preValue", dto);
        }else{
            model.addAttribute("preValue", new RegistUserRequestDto());
        }
        model.addAttribute("notice", exception.getMessage());
        return "myuser/regist_form";
    }
    @ExceptionHandler(UserLoginFaildException.class)
    public String handldeUserLoginFaildException(UserLoginFaildException exception, Model model){
        model.addAttribute("notice", exception.getMessage());
        return "myuser/login";
    }
    @ExceptionHandler(UserProfileImageUploadException.class)
    public String handleUserProfileImageUploadException(
        UserProfileImageUploadException exception, 
        HttpServletRequest request,
        Model model){

        String requestUri = request.getRequestURI();
        if(requestUri.contains("regist")){
            model.addAttribute("preValue", exception.getRegistUserReqeustDto());
            model.addAttribute("notice", exception.getMessage());
            return "myuser/regist_form";
        }else if(requestUri.contains("edit")){
            model.addAttribute("preValue", exception.getEditUserRequestDto());
            model.addAttribute("notice", exception.getMessage());
            return "myuser/mypage_form";
        }

        return "error";
    }
    @ExceptionHandler(UserNotFoundException.class)
    public String handelUserNotFoundException(UserNotFoundException exception, HttpServletRequest request, Model model){
        request.getSession().invalidate();
        model.addAttribute("notice", exception.getMessage());
        
        return "home";
    }

    // dto 검증 시 발생 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception, 
        HttpServletRequest request,
        Model model)
    {
        // 메시지 내용 수집
        String noticeMessage = exception.getBindingResult().getAllErrors()
                                        .stream()
                                        .map(ObjectError::getDefaultMessage)
                                        .collect(Collectors.joining("\n"));

        model.addAttribute("notice", noticeMessage);
        
        // dto 내용을 다시 set해주기 
        Object dtoObejct = exception.getBindingResult().getTarget();
        if(dtoObejct != null){
            model.addAttribute("preValue", dtoObejct);
        }

        // String referer = request.getHeader("Referer");
        String requestUri = request.getRequestURI();
        if(requestUri != null && requestUri.contains("regist")){
            return "myuser/regist_form";
        }else if(requestUri != null && requestUri.contains("login")){
            return "myuser/login";
        }else if(requestUri != null && requestUri.contains("edit")){
            return "myuser/mypage_form";
        }

        return "error";
    }
    // 정의 외 에러 발생 
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException exception){
        return "error";
    }
}
    