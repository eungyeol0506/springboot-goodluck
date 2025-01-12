package com.example.goodluck.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@ControllerAdvice
public class GlobalExceptionHandler {
    // @ExceptionHandler(UserNotFoundException.class)
    // public String handelUserNotFoundException(UserNotFoundException ex, RedirectAttributes redirectAttributes){
    //     redirectAttributes.addFlashAttribute("message", ex.getMessage());
    //     // model.addAttribute("message", ex.getMessage());
    //     return "redirect:/login";
    // }
    @ExceptionHandler(UserNotFoundLoginException.class)
    public String handelUserNotFoundLoginException(UserNotFoundLoginException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", ex.getMessage());
        return "redirect:/login";
    }
    
    @ExceptionHandler(UserProfileImageUploadException.class)
    public String handleUserProfileImageUploadException(
            UserProfileImageUploadException exception,
            RedirectAttributes redirectAttributes
        ){
        redirectAttributes.addFlashAttribute("message", exception.getMessage());
        return "redirect:/regist";
    }

    @ExceptionHandler(InvalidUserNoException.class)
    public String handleInvalidUserNoException(InvalidUserNoException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }
}
