package com.example.goodluck.domain;

import java.util.List;

public interface AttachRepository {
    List<MyAttach> selectAttacheList(Long boardNo);
    void insertAll(List<MyAttach> files);
    
    int updateAttach(MyAttach attach);
    int deleteAttach(MyAttach attach);
    int deleteAttachList(List<MyAttach> attachList);
}
