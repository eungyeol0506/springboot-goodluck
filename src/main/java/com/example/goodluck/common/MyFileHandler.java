package com.example.goodluck.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.domain.MyUser;

public class MyFileHandler{
    // 사용자 이미지 프로필 파일 경로
    public static final String PROFILE_DIR_STRING = "src/main/resources/static/files";
    public static final Path PROFILE_DIR = Paths.get(PROFILE_DIR_STRING);

    public Optional<String> uploadMyUserProfileImage(
            @NonNull
            MultipartFile multipartFile, 
            MyUser myUser
        ){

        String fileExtension = getFileExtension(multipartFile);
        String fileName = myUser.getUserId() + "." + fileExtension;
        
        try{
            // create new dir
            if (!Files.exists(PROFILE_DIR)) {
                Files.createDirectories(PROFILE_DIR);
            }

            Path filePath = PROFILE_DIR.resolve(fileName);
            // get file data
            InputStream inputStream = multipartFile.getInputStream();
            // copy to server disk
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        } catch(IOException exception){
            exception.printStackTrace();
            return Optional.empty();
            // throw new UserRegistFaildException("이미지 업로드에 실패하였습니다.");          
        }

        return Optional.ofNullable(fileName);
    }

    private String getFileExtension(@NonNull MultipartFile multipartFile){
        String originalFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
        return extension;
    }     
}