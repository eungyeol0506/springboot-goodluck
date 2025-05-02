package com.example.goodluck.domain;

import java.util.List;

public interface AttachRepository {
    public List<MyAttach> findByBoardNo(Long boardNo);
    public void saveAll(List<MyAttach> files);
    // int update(MyAttach attach);
    public void remove(List<Long> attaches);
}
