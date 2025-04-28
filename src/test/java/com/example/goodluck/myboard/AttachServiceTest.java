package com.example.goodluck.myboard;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.domain.AttachRepository;
import com.example.goodluck.domain.MyBoard;

@ExtendWith(MockitoExtension.class)
public class AttachServiceTest {

    @Mock
    private AttachRepository mockRepository;

    @InjectMocks
    private AttachService attachService;

    @Nested
    @DisplayName("파일 저장 테스트")
    class uploadAttachList{
        
        @Test
        @DisplayName("파일을 저장하는 경우")
        void successUploadAttachList() throws IOException{
            // given
            // 생성할 폴더명
            long boardNo = 9998L;

            List<MultipartFile> fileList = new ArrayList<>();
            for(int i=0; i<2; i++){
                MockMultipartFile file = new MockMultipartFile("test" + i , "test_" + i + ".txt", "image/png", "Hi, Hello".getBytes());
                fileList.add(file);
            }
            MyBoard testBoard = MyBoard.createDummy(boardNo, "test", "test", 1L);
            
            BDDMockito.willDoNothing().given(mockRepository).insertAll(anyList());
            // when
            attachService.uploadAttachList(testBoard, fileList);
            // then
            BDDMockito.then(mockRepository).should().insertAll(anyList());
            Assertions.assertThat(Files.exists(Paths.get("src/main/resources/static/files/attach/"+boardNo)));
        }

        @Test
        @DisplayName("IOException 발생의 경우")
        void failUploadAttachListCauseIOException(){
            // AttachService code 수정이 필요. (추후 수정필요 있음)

            // given
            // 생성할 폴더명
            long boardNo = 9998L;

            List<MultipartFile> fileList = new ArrayList<>();
            for(int i=0; i<2; i++){
                MockMultipartFile file = new MockMultipartFile("test" + i , "test_" + i + ".txt", "image/png", "Hi, Hello".getBytes());
                fileList.add(file);
            }
            MyBoard testBoard = MyBoard.createDummy(boardNo, "test", "test", 1L);
            
            // BDDMockito.willDoNothing().given(mockRepository).insertAll(anyList());
            // when
            Exception exception = Assertions.catchException(() -> attachService.uploadAttachList(testBoard, fileList));
            // then
            Assertions.assertThat(exception).isInstanceOf(IOException.class);
            BDDMockito.then(mockRepository).should(never()).insertAll(anyList());
        }
        @Test
        @DisplayName("Repository에서 Exception 발생의 경우")
        void failUploadAttachListCauseElseException(){
            // given
            // 생성할 폴더명
            long boardNo = 9998L;

            List<MultipartFile> fileList = new ArrayList<>();
            for(int i=0; i<2; i++){
                MockMultipartFile file = new MockMultipartFile("test" + i , "test_" + i + ".txt", "image/png", "Hi, Hello".getBytes());
                fileList.add(file);
            }
            MyBoard testBoard = MyBoard.createDummy(boardNo, "test", "test", 1L);
            BDDMockito.willThrow(new IllegalStateException()).given(mockRepository).insertAll(anyList());
            // when
            Exception exception = Assertions.catchException(() -> attachService.uploadAttachList(testBoard, fileList));
            // then
            Assertions.assertThat(exception).isInstanceOf(IllegalStateException.class);
            BDDMockito.then(mockRepository).should().insertAll(anyList());
        }
    }

//  service에서 조회하는 메서드의 경우 repository 값을 그대로 넘기므로, controller에서 테스트

}
