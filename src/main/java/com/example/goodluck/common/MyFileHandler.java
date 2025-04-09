package com.example.goodluck.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public abstract class MyFileHandler {
    protected String FILE_DIR = "src/main/resources/static/files";

    // 확장자 구하기
    protected String getFileExtension(MultipartFile multipartFile){
        String originName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String extension = originName.substring(originName.lastIndexOf(".") + 1);

        return extension;
    }

    // 폴더 생성
    protected Path getDirPath(String dirName) throws IOException{
        Path dirPath = Paths.get(dirName);
        if(!Files.exists(dirPath)){
            Files.createDirectories(dirPath);
        }

        return dirPath;
    } 

    // 파일을 디스크에 저장
    public abstract void saveFile(MultipartFile multipartFile) throws IOException;
    
    public void saveFileTest(MultipartFile multipartFile) throws IOException{
        throw new IOException("IOException 처리 테스트");
    }
    
} 
