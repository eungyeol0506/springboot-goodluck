package com.example.goodluck.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyAttachTest {
    @Test
    void testBuilder() {
        MyAttach result = MyAttach.builder()
                            .fileName("테스트파일")
                            .filePath("테스트경로")
                            .fileSize(1024)
                            .build();

        Assertions.assertThat(result);

        MyAttach result2 = MyAttach.builder()
                            .fileName("테스트파일")
                            .build();

        Assertions.assertThat(result2);
        MyAttach result3 = MyAttach.builder()
                            .fileName("테스트파일")
                            .fileSize(1024)
                            .build();

        Assertions.assertThat(result3);
    }
}
