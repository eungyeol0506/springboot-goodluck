package com.example.goodluck.domain;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

// import org.springframework.web.multipart.MultipartFile;

@Data
public class MyBoard {
    private long boardNo;
    private String boardTitle;
    private String contents;
    private int viewCnt;
    private LocalDate updateDate;
    private LocalDate createDate;
    
    private MyUser user;
    private List<MyAttach> attachList;
    private List<MyComment> comments;

    public MyBoard() {};
    public MyBoard(Long boardNo, String boardTitle, String contents, int viewCnt, LocalDate updateDate, LocalDate createDate,
            MyUser user) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.contents = contents;
        this.viewCnt = viewCnt;
        this.updateDate = updateDate;
        this.createDate = createDate;
        this.user = user;
    }
    
    public void setWriterInfo(MyUser writer){
        this.user = writer;
    }

    @Transient
    public void increaseViewCnt(){
        this.viewCnt += 1;
    }
    @Transient
    public static MyBoard createDummy(Long boardNo, String boardTitle, String contens, Long userNo){
        return new MyBoard(boardNo, boardTitle, contens, 0, null, LocalDate.now(), MyUser.creatDummy(userNo));
    }
    
}
