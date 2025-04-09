package com.example.goodluck.myboard;

import java.util.List;

import com.example.goodluck.domain.MyAttach;

public interface AttachRepository {
    List<MyAttach> selectAttacheList(Long boardNo);
    void insertAll(List<MyAttach> files);
    
    int updateAttach(MyAttach attach);
    int deleteAttach(MyAttach attach);
    int deleteAttachList(List<MyAttach> attachList);
}
