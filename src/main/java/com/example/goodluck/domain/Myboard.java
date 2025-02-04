package com.example.goodluck.domain;

import java.sql.Date;
// import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

// import org.springframework.web.multipart.MultipartFile;

@Data
public class Myboard {
    private long boardNo;
    private String boardTitle;
    private String contents;
    private int viewCnt;
    private LocalDate updateDate;
    private LocalDate createDate;
    
    private MyUser user;
    // private List<MultipartFile> attachFiles;
    // private List<MyAttach> attaches;
    
    public Myboard() {};
    public Myboard(long boardNo, String boardTitle, String contents, int viewCnt, LocalDate updateDate, LocalDate createDate,
            MyUser user) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.contents = contents;
        this.viewCnt = viewCnt;
        this.updateDate = updateDate;
        this.createDate = createDate;
        this.user = user;
    }
    
}
