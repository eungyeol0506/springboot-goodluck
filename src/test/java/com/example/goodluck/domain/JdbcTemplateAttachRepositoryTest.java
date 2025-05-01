package com.example.goodluck.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
// @Transactional
public class JdbcTemplateAttachRepositoryTest {

    @Autowired JdbcTemplateAttachRepository attachRepository;

    @Test
    @DisplayName("저장에 성공한 경우")
    @Order(1)
    void successSave(){
        // given
        List<MyAttach> attaches = new ArrayList<>();
        for(int i=0; i<3; i++){
            MyAttach temp = getTestAttach();
            temp.setFileName("Test 1");
            attaches.add(temp);
        }

        // when
        attachRepository.saveAll(attaches);

        // then
        List<MyAttach> result = attachRepository.findByBoardNo(9999L);
        assertFalse(result.isEmpty(), "빈 리스트가 아님");
        assertEquals(attaches.size(), result.size(), "입력한 만큼 값이 저장됨");
    }

    @Test
    @DisplayName("삭제에 성공한 경우")
    @Order(2)
    void successRemove(){
        // given (save 실행 해야 성공 가능... )
        List<MyAttach> findAttaches = attachRepository.findByBoardNo(9999L);
        List<Long> attachNos = new ArrayList<>();
        for(MyAttach attach : findAttaches){
            attachNos.add(attach.getAttachNo());
        }

        // when
        attachRepository.remove(attachNos);

        // then
        List<MyAttach> result = attachRepository.findByBoardNo(9999L);
        assertTrue(result.isEmpty(), "삭제되어서 빈 리스트임");
    }
    
    private MyAttach getTestAttach() {
        return MyAttach.builder()
                .boardNo(9999L)
                .fileName("Test")
                .filePath("uploads")
                .fileSize(1024L)
                .build();
    }



}
