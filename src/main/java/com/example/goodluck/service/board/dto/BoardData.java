package com.example.goodluck.service.board.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class BoardData {
    
    private Long boardNo;
    private String boardTitle;
    private String contents;
    private int viewCnt;
    private LocalDate lastUpdateDate;
    private String writerName;
    private List<String> attachPaths = new ArrayList<>();
    private List<CommentData> comments = new ArrayList<>();

    @Setter(AccessLevel.PROTECTED)
    @Getter
    class CommentData {
        private String writerName ;
        private String reply;
        private LocalDate createDate; 
    }

    public void addCommentData(String userName, String reply, LocalDate createDate){
        CommentData commenData = new CommentData();
        commenData.setWriterName(userName);
        commenData.setReply(reply);
        commenData.setCreateDate(createDate);

        this.comments.add(commenData);
    }

}
