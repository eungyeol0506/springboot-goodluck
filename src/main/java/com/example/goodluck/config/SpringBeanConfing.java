package com.example.goodluck.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.goodluck.domain.AttachRepository;
import com.example.goodluck.domain.BoardRepository;
import com.example.goodluck.domain.JdbcTemplateAttachRepository;
import com.example.goodluck.domain.JdbcTemplateBoardRepository;
import com.example.goodluck.domain.JdbcTemplateCommentRepository;
import com.example.goodluck.domain.JdbcTemplateUserRepository;
import com.example.goodluck.domain.UserRepository;

@Configuration
public class SpringBeanConfing {
    
    private DataSource dataSource;

    public SpringBeanConfing(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserRepository userRepository(){
        return new JdbcTemplateUserRepository(dataSource);
    }
    
    @Bean
    public BoardRepository boardRepository(){
        return new JdbcTemplateBoardRepository(dataSource);
    }

    @Bean
    public AttachRepository attachRepository(){
        return new JdbcTemplateAttachRepository(dataSource);
    }

    @Bean
    public JdbcTemplateCommentRepository commentRepository(){
        return new JdbcTemplateCommentRepository(dataSource);
    }

}
