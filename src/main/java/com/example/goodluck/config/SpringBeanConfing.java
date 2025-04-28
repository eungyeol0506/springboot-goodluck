package com.example.goodluck.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.goodluck.domain.AttachRepository;
import com.example.goodluck.domain.BoardRepository;
import com.example.goodluck.domain.JdbcTemplateAttackRepository;
import com.example.goodluck.domain.JdbcTemplateBoardRepository;
import com.example.goodluck.domain.JdbcTemplateCommentRepository;
import com.example.goodluck.myboard.AttachService;
import com.example.goodluck.myboard.BoardService;
import com.example.goodluck.myboard.CommentService;
import com.example.goodluck.myuser.JdbcTemplateUserRepository;
import com.example.goodluck.myuser.UserRepository;
import com.example.goodluck.myuser.UserService;

@Configuration
public class SpringBeanConfing {
    
    private DataSource dataSource;

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
    
    @Bean
    public BoardService boardService(){
        return new BoardService(boardRepository());
    }
    @Bean
    public BoardRepository boardRepository(){
        return new JdbcTemplateBoardRepository(dataSource);
    }

    @Bean
    public AttachService attachService(){
        return new AttachService(attachRepository());
    }
    @Bean
    public AttachRepository attachRepository(){
        return new JdbcTemplateAttackRepository(dataSource);
    }

    @Bean
    public JdbcTemplateCommentRepository commentRepository(){
        return new JdbcTemplateCommentRepository(dataSource);
    }
    @Bean
    public CommentService commentService(){
        return new CommentService(commentRepository());
    }
}
