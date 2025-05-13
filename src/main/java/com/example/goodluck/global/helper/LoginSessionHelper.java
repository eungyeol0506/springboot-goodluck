package com.example.goodluck.global.helper;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.goodluck.domain.AuthUser;

/*
 * SecurityContextHolder 기반 인증 확인을 도와주는 클래스
 */
public class LoginSessionHelper {
    
    public static boolean isValidate(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            return false;
        }

        return true;
    }

    public static Optional<AuthUser> getAuthUser(){
        if(!isValidate()) return Optional.empty();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable((AuthUser) auth.getPrincipal());
    }

    public static Long getUserNo(){

        AuthUser authUser = getAuthUser().orElse(null);
        if(authUser == null){
            return 0L;
        }
        
        return authUser.getUserNo();
    }
}
