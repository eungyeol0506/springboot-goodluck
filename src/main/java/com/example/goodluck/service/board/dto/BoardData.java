package com.example.goodluck.service.board.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.goodluck.domain.MyComment;

import lombok.Data;

@Data
public class BoardData {
    
    private Long boardNo;
    private String boardTitle;
    private String contents;
    private int viewCnt;
    private LocalDate lastUpdateDate;
    private String writerName;
    private List<String> attachPaths = new ArrayList<>();
    private List<MyComment> comments = new ArrayList<>();

}
