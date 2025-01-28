package com.example.goodluck.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.goodluck.myuser.JdbcTemplateUserRepository;
import com.example.goodluck.myuser.UserRepository;
import com.example.goodluck.myuser.UserService;

@Configuration
public class SpringBeanConfing {
    
    private DataSource dataSource;

    @Autowired
    public SpringBeanConfing(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Bean
    public UserService userService() {
        return new UserService(userRepository());
    }

    @Bean
    public UserRepository userRepository(){
        return new JdbcTemplateUserRepository(dataSource);
    }

}
