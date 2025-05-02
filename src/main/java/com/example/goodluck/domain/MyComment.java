package com.example.goodluck.domain;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyComment {
    // pk
    private Long commentNo;
    
    private Long userNo;
    private Long boardNo;
    private String reply;
    private LocalDate createDate;

    // DB 테이블, 시퀀스 이름
    @RequiredArgsConstructor
    public enum CommentConstants{
        PRIVATE_KEY("COMMENT_NO"),
        TABLE_NAME("MY_COMMENT"),
        SEQUENCE_NAME("MY_COMMENT_SEQ");

        private final String value;

        public String getValue(){
            return value;
        }
    }
}
