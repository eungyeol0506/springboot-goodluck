package com.example.goodluck.exception;

import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @ExceptionHandler(UserNotFoundException.class)
    public String handelUserNotFoundException(UserNotFoundException exception, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", exception.getMessage());
        // model.addAttribute("message", ex.getMessage());
        return "redirect:/";
    }
    @ExceptionHandler(UserNotFoundLoginException.class)
    public String handelUserNotFoundLoginException(UserNotFoundLoginException exception, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", exception.getMessage());
        return "redirect:/login";
    }
        
    @ExceptionHandler(InvalidUserNoException.class)
    public String handleInvalidUserNoException(InvalidUserNoException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "error";
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
        }
        return "error";
    }
    // 정의 외 에러 발생 
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException exception){
        return "error";
    }
}
    