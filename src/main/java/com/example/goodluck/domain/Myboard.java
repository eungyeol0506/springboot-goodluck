package com.example.goodluck.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Setter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Setter
@Builder
public class MyBoard {
    // pk
    private Long boardNo;

    private String boardTitle;
    private String contents;
    private int viewCnt;
    private LocalDate updateDate;
    private LocalDate createDate;
    
    // 참조
    private MyUser writer;
    @Builder.Default
    private List<MyAttach> attaches = new ArrayList<>();
    @Builder.Default
    private List<MyComment> comments = new ArrayList<>();

    @RequiredArgsConstructor
    public enum BoardConstants{
        PRIVATE_KEY("BOARD_NO"),
        TABLE_NAME("MY_BOARD"),
        SEQUENCE_NAME("MY_BOARD_SEQ");

        private final String value;
        
        public String getValue(){
            return value;
        }
    }
    
}
