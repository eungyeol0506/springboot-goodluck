package com.example.goodluck.exception;

import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.goodluck.exception.myuser.UserProfileImageUploadException;
import com.example.goodluck.exception.myuser.UserRegistFaildException;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserRegistFaildException.class)
    public String handleUserRegistFaildException(UserRegistFaildException exception, RedirectAttributes redirectAttributes)
    {
        // model.addAttribute("notice", exception.getMessage());
        redirectAttributes.addFlashAttribute("notice", exception.getMessage());
        return "redirect:/regist";
        // return "regist";
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, RedirectAttributes redirectAttributes )
    {
        String noticeMessage = "";
        for( ObjectError e : exception.getBindingResult().getAllErrors()){
            noticeMessage += e.getDefaultMessage() + "\n";
        }
        redirectAttributes.addFlashAttribute("notice", noticeMessage);
        return "regist";
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
    @ExceptionHandler(UserProfileImageUploadException.class)
    public String handleUserProfileImageUploadException(
            UserProfileImageUploadException exception,
            RedirectAttributes redirectAttributes
        ){
        redirectAttributes.addFlashAttribute("notice", exception.getMessage());
        return "redirect:/regist";
        // return null;
    }

    @ExceptionHandler(InvalidUserNoException.class)
    public String handleInvalidUserNoException(InvalidUserNoException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "error";
    }
}
