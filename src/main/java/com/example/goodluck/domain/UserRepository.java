package com.example.goodluck.domain;

import java.util.Optional;

public interface UserRepository {
    public Long save(MyUser user);    
    public Optional<MyUser> findByNo(Long no); 
    public Optional<MyUser> findById(String id);
    public void remove(Long no);
    public void update(MyUser user);
}
