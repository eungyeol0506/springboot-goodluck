package com.example.goodluck.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 보통 가장 많이 사용하는 인코더
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth -> 
                auth
                    .requestMatchers("/profile", "/profile/form").authenticated()
                    .requestMatchers("/board/write", "/board/form").authenticated()
                    .anyRequest().permitAll()
                )
                .formLogin(form ->
                    form.disable()
                )
                .logout(logout ->
                    logout.disable())
                ;

        return http.build();
    }
}