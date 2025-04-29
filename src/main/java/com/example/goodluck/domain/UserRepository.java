package com.example.goodluck.domain;

import java.util.Optional;

public interface UserRepository {
    public void save(MyUser user);    
    public Optional<MyUser> findByNo(Long no); 
    public Optional<MyUser> findById(String id);
    // public List<MyUser> findAll();
    public void remove(Long no);
    public void update(MyUser user);
}
