package com.example.goodluck.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.global.FilePathHelper;
import com.example.goodluck.global.FileStorageService;
import com.example.goodluck.global.SaveType;

@Service
public class LocalFileStorageService implements FileStorageService{
    @Value("${file.upload-dir}")
    private String uploadDirName;

    @Override
    public void delete(String relativePath) {
        try{
            Path fullPath = Paths.get(relativePath);

            Files.deleteIfExists(fullPath);
        }catch(IOException e){
            // nothing...
        }
    }

    @Override
    public String save(MultipartFile image, Long no, SaveType saveType) {
        try{
            String relativePathName = getSavePath(saveType, no, image.getOriginalFilename());
            System.out.println("Original filename: " + image.getOriginalFilename());

            Path fullPath = Paths.get(uploadDirName).resolve(relativePathName);
            System.out.println("Full save path: " + fullPath.toString());

            if(Files.notExists(fullPath.getParent())){
                Files.createDirectories(fullPath.getParent());
            }

            image.transferTo(fullPath.toFile());

            return fullPath.toString().replace("\\", "/");

        }catch(IOException e){
            e.printStackTrace();
            return "FAILED";
        }
    }
    
    private String getSavePath(SaveType saveType, Long no, String originalFileName){
        if(saveType == SaveType.PROFILE){
            return FilePathHelper.generateProfileName(originalFileName);
        }
        else if(saveType == SaveType.BOARD){
            return FilePathHelper.generateAttachName(no, originalFileName);
        }
        throw new IllegalArgumentException();
    }

}
