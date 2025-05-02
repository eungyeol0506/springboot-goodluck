// package com.example.goodluck.common;

// import java.io.IOException;

// import org.assertj.core.api.Assertions;
// import org.junit.jupiter.api.Test;
// import org.springframework.mock.web.MockMultipartFile;

// import com.example.goodluck.domain.MyUser;
// import com.example.goodluck.global.MyUserProfileImageHandler;

// public class MyUserProfileImageHandlerTest {
//     @Test
//     void testBuilder() {
//         // given
//         MyUser testUser = MyUser.creatDummy(1L);
//         // when
//         MyUserProfileImageHandler fileHandler = MyUserProfileImageHandler.builder()
//                                                                          .user(testUser)
//                                                                          .build();
//         // then                                                                         
//         Assertions.assertThat(fileHandler);
//     }

//     @Test
//     void testSaveFile() {
//         // given
//         MockMultipartFile multipartFile = new MockMultipartFile("test", "test.txt", "image/png", "Hi, Hello".getBytes());
//         MyUser testUser = MyUser.creatDummy(12345L);
//         MyUserProfileImageHandler fileHandler = MyUserProfileImageHandler.builder()
//                                                                          .user(testUser)
//                                                                          .build();
//         // when
//         try{
//             fileHandler.saveFile(multipartFile);
//         }catch(IOException e) {
//             e.printStackTrace();
//         }
//         // then 
//         Assertions.assertThat(fileHandler);
//         Assertions.assertThat(fileHandler.getFileExtension(multipartFile));
//     }
// }
