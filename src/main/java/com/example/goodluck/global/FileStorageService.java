package com.example.goodluck.global;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    public String save(MultipartFile file, Long no, SaveType saveType);
    public void delete(String relativePath);
} 
