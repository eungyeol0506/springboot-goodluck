package com.example.goodluck.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@ControllerAdvice
public class GlobalExceptionHandler {
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
        redirectAttributes.addFlashAttribute("message", exception.getMessage());
        return "redirect:/regist";
    }

    @ExceptionHandler(InvalidUserNoException.class)
    public String handleInvalidUserNoException(InvalidUserNoException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "error";
    }
}
