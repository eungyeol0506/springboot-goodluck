// package com.example.goodluck.common;

// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;

// import org.assertj.core.api.Assertions;
// import org.junit.jupiter.api.Test;
// import org.springframework.mock.web.MockMultipartFile;

// import com.example.goodluck.domain.MyBoard;
// import com.example.goodluck.global.MyBoardAttachFileHandler;

// public class MyBoardAttachFileHandlerTest {
//     @Test
//     void testBuilder() {
//         // given
//         MyBoard testBoard = MyBoard.createDummy(9999L, "test", "test", 1L);
//         // when
//         MyBoardAttachFileHandler fileHandler = MyBoardAttachFileHandler.builder()
//                                                                         .board(testBoard)
//                                                                         .build();
//         // then
//         Assertions.assertThat(fileHandler);
//     }

//     @Test
//     void testSaveFile() throws IOException{
//         // given
//         List<MockMultipartFile> fileList = new ArrayList<>();
//         for(int i=0; i<3; i++){
//             MockMultipartFile file = new MockMultipartFile("test" + i , "test_" + i + ".txt", "image/png", "Hi, Hello".getBytes());
//             fileList.add(file);
//         }
//         MyBoard testBoard = MyBoard.createDummy(9999L, "test", "test", 1L);
//         MyBoardAttachFileHandler fileHandler = MyBoardAttachFileHandler.builder()
//                                                                         .board(testBoard)
//                                                                         .build();
//         // when
//         for(MockMultipartFile file : fileList){
//             fileHandler.saveFile(file);
//         }
//         // then
//         Assertions.assertThat(fileHandler);
        
//     }
// }
