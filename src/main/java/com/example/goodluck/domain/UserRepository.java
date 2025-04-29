package com.example.goodluck.domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    public void save(MyUser user);    
    public Optional<MyUser> findByNo(Long no); 
    public Optional<MyUser> findById(String id);
    public List<MyUser> selectAll();
    public void remove(Long no);
    public void update(MyUser user);
}
