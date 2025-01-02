package com.example.goodluck.domain;

import java.sql.Date;
// import java.util.List;

// import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
