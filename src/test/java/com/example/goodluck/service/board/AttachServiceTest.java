package com.example.goodluck.service.board;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.domain.MyAttach;

@SpringBootTest
@Transactional
public class AttachServiceTest {

    @Autowired AttachService attachService;

    @Test
    @DisplayName("첨부파일 저장 성공")
    void successUpload(){
        // given
        Long userNo = 9999L;
        List<MultipartFile> files = new ArrayList<>();
        for(int i=0; i<3; i++){
            MultipartFile testFile = new MockMultipartFile("file_" + i, "test.txt", "text/plain", "테스트 내용".getBytes(StandardCharsets.UTF_8));
            files.add(testFile);
        }

        // when
        attachService.upload(userNo, files);

        // then
        List<MyAttach> attaches = attachService.getAttachList(userNo);
        assertNotNull(attaches);
        assertTrue(Files.isDirectory(Paths.get("./uploads/attaches/9999")));

    }


}

