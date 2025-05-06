package com.example.goodluck.global.exception;

import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.goodluck.global.ServiceExcepction;
import com.example.goodluck.service.user.dto.UserLoginRequest;

import jakarta.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalExceptionHandler {
    /*
     * Business Logic 예외 처리
     */
    @ExceptionHandler(ServiceExcepction.class)
    public String handleServiceException(
        ServiceExcepction excepction,
        HttpServletRequest request,
        Model model
    ){
        model.addAttribute("notice", excepction.getErrorMessage());
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
        addBindingResultToModel(exception.getBindingResult(), model);

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
     * preValue setting
     */
    private void addBindingResultToModel(BindingResult bindingResult, Model model){
        // 메시지 내용 수집
        String noticeMessage = bindingResult.getAllErrors()
                                .stream()
                                .map(ObjectError::getDefaultMessage)
                                .collect(Collectors.joining("\n"));

        model.addAttribute("notice", noticeMessage);

        // dto 내용을 다시 set해주기 
        Object dtoObejct = bindingResult.getTarget();
        if(dtoObejct != null){
        model.addAttribute("preValue", dtoObejct);
        }
    }

    class ExceptionViewHelper{

        public static String resolveViewNameByUri(String requestUri, Model model){
            if(requestUri != null && requestUri.contains("regist")){
                return "user/regist";
            }
            else if(requestUri != null && requestUri.contains("login")){
                model.addAttribute("loginRequest", new UserLoginRequest());
                return "user/login";
            }
            else if(requestUri != null && requestUri.contains("edit")){
                return "user/edit";
            }
            else if(requestUri != null && requestUri.contains("change-password")){
                return "user/pwChange";
            }
            else if(requestUri != null && requestUri.contains("write")){
                return "board/write";
            }
            return "error";
        }
    }
}
    