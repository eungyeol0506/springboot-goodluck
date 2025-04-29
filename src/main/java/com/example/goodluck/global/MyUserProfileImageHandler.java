package com.example.goodluck.global;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.domain.MyUser;

import lombok.Builder;


public class MyUserProfileImageHandler extends MyFileHandler {
    private String profileDirName;
    private String userProfileFileName;
    private MyUser user;

    @Builder
    public MyUserProfileImageHandler(MyUser user){
        this.profileDirName = super.FILE_DIR + "/profile";
        this.user = user;
    }

    @Override
    public void saveFile(MultipartFile multipartFile) throws IOException{
        if(multipartFile == null){
            return;
        }
        // 저장할 파일 명
        userProfileFileName = user.getUserId() + "." + super.getFileExtension(multipartFile);
        // 저장될 파일 경로
        Path userProfileFilePath = super.getDirPath(profileDirName).resolve(userProfileFileName);

        // get File Data
        InputStream inputStream = multipartFile.getInputStream();
        // copy Data 한개만 저장하기 때문에, 항상 덮어씌움
        Files.copy(inputStream, userProfileFilePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public String getFileName(){
        return this.userProfileFileName;
    }
    public String getDirName(){
        return this.profileDirName;
    }
}
