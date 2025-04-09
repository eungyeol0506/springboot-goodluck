package com.example.goodluck.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyAttach {
    private long attachNo;
    private String fileName;
    private String filePath;
    private long fileSize;
    private long boardNo;

    @Builder
    public MyAttach(long attachNo, String fileName, String filePath, long fileSize){
        this.attachNo = attachNo;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
    public void setNewAttachNo(long attachNo){
        this.attachNo = attachNo;
    }
    public void setBoardNo(long boardNo){
        this.boardNo = boardNo ;
    }
}
