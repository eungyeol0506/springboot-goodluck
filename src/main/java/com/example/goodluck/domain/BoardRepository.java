package com.example.goodluck.domain;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    public Optional<MyBoard> findByNo(Long boardNo);
    public List<MyBoard> findAll(Long start, Long end);
    public Long save(MyBoard newBoard);
    public void update(MyBoard board);
    public void remove(Long boardNo);
    public Long getAllCount();
}
