package com.example.goodluck.domain;

import java.util.List;

public interface CommentRepository {
    public List<MyComment> findByBoardNo(Long boardNo);
    public List<MyComment> findByUserNo(Long userNo);
    public void save(MyComment comment);
    public void remove(List<Long> comments);
}
