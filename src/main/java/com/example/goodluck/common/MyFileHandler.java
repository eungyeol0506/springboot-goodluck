package com.example.goodluck.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.exception.UserProfileImageUploadException;

public class MyFileHandler{
    // 사용자 이미지 프로필 파일 경로
    public static final String PROFILE_DIR = "src/main/resources/files";

    public Optional<String> saveSingleFileFromMultipartFile(
                                MultipartFile multipartFile, 
                                String uploadPath,
                                String saveFileName
        ){
        // return value
        String fileName = "";

        if( !multipartFile.isEmpty()){
            String fileExtension = getFileExtension(multipartFile);
            fileName = saveFileName + "." + fileExtension;

            Path filePath = Paths.get(uploadPath).resolve(fileName);

            // get file data
            try{
                InputStream inputStream = multipartFile.getInputStream();
                
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch(IOException exception){
                throw new UserProfileImageUploadException("이미지 업로드 실패!");
            }
        }

        return Optional.ofNullable(fileName);
    }

    private String getFileExtension(MultipartFile multipartFile){
        String originalFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
        return extension;
    }     
}