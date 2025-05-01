package com.example.goodluck.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.global.SaveType;

@SpringBootTest
public class LocalStorageFileServiceTest {

    @Autowired LocalFileStorageService fileService;

    @Test
    @DisplayName("파일 저장에 성공")
    void successSave(){ 
        // given
        MultipartFile testFile = new MockMultipartFile("file", "test.txt", "text/plain", "테스트 내용".getBytes(StandardCharsets.UTF_8));
        
        // when
        String result1 = fileService.save(testFile, 1L, SaveType.PROFILE);
        String result2 = fileService.save(testFile, 1L, SaveType.BOARD);

        // then
        assertNotEquals("파일 저장 실패", result1);
        assertNotEquals("파일 저장 실패", result2);
        assertTrue(Files.exists(Paths.get(result1)));
        assertTrue(Files.exists(Paths.get(result2)));
        
    }

    @Test
    @DisplayName("파일 삭제에 성공")
    void successDelete(){
        // given
        MultipartFile testFile = new MockMultipartFile("file", "test.txt", "text/plain", "테스트 내용".getBytes(StandardCharsets.UTF_8));
        
        String result1 = fileService.save(testFile, 1L, SaveType.PROFILE);
        String result2 = fileService.save(testFile, 1L, SaveType.BOARD);

        // when
        fileService.delete(result1);
        fileService.delete(result2);

        // then
        assertTrue(Files.notExists(Paths.get(result1)));
        assertTrue(Files.notExists(Paths.get(result2)));
    }
}
