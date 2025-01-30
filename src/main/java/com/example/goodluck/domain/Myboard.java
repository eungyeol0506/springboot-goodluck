package com.example.goodluck.domain;

import java.sql.Date;
// import java.util.List;

import lombok.Data;

// import org.springframework.web.multipart.MultipartFile;

@Data
public class Myboard {
    private long boardNo;
    private String boardTitle;
    private String contents;
    private int viewCnt;
    private Date updateDate;
    private Date createDate;
    private MyUser user;
    // private List<MultipartFile> attachFiles;
    // private List<MyAttach> attaches;
}
