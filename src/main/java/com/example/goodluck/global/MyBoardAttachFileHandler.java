package com.example.goodluck.global;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.domain.MyAttach;
import com.example.goodluck.domain.MyBoard;

import lombok.Builder;

public class MyBoardAttachFileHandler extends MyFileHandler{
    private String attachDirName;
    private MyBoard board;

    private MyAttach tempAttach;

    @Builder
    public MyBoardAttachFileHandler(MyBoard board){
        this.attachDirName = super.FILE_DIR + "/attach";
        this.board = board;
    }

    @Override
    public void saveFile(MultipartFile multipartFile) throws IOException {
            if(multipartFile == null){
                return;
            }
            clearTempAttach();
            
            // 저장될 파일 경로 - 보드No로 폴더 경로를 하나 더 생성해줌
            String boardDirName;
            boardDirName = this.attachDirName + "/" + this.board.getBoardNo();

            Path boardFilePath = super.getDirPath(boardDirName).resolve(multipartFile.getOriginalFilename());
            
            // get File Data
            InputStream inputStream = multipartFile.getInputStream();
            // copy Data 한개만 저장하기 때문에, 항상 덮어씌움
            Files.copy(inputStream, boardFilePath, StandardCopyOption.REPLACE_EXISTING);

            this.tempAttach = MyAttach.builder()
                                      .fileName(multipartFile.getOriginalFilename())
                                      .filePath(boardDirName)
                                      .fileSize(multipartFile.getSize())
                                      .build();

    }
    public MyAttach getLastMyAttach(){
        return this.tempAttach;
    }
    public String getDirName(){
        return this.attachDirName + this.board.getBoardNo();
    }
    private void clearTempAttach(){
        this.tempAttach  = null;
    }
}
