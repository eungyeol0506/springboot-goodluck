package com.example.goodluck.myboard;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.goodluck.domain.JdbcTemplateAttackRepository;
import com.example.goodluck.domain.MyAttach;

@SpringBootTest
public class JdbcTemplateAttachRepositoryTest {

    @Autowired
    JdbcTemplateAttackRepository repository;

    @Nested
    @DisplayName("첨부 이미지 DB 데이터 저장 테스트")
    class insertAll{

        @DisplayName("bulk insert가 성공한 경우")
        @Test
        public void successAll(){
            // given
            List<MyAttach> files = new ArrayList<>();
            for(int i=0; i<3; i++){
                MyAttach file = MyAttach.builder()
                                        .fileName("테스트" + i)
                                        .filePath("테스트 경로")
                                        .fileSize(1024)
                                        .build();
                file.setBoardNo(1L);
                
                files.add(file);
            }            
            // when
            repository.insertAll(files);
            // then
            for(MyAttach file : files){
                Assertions.assertThat(file.getAttachNo());
            }
        }

    }
}
