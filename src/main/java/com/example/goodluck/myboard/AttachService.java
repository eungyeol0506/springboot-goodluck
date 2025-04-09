package com.example.goodluck.myboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.common.MyBoardAttachFileHandler;
import com.example.goodluck.domain.MyAttach;
import com.example.goodluck.domain.MyBoard;

public class AttachService {
    
    private AttachRepository attachRepository;

    @Autowired
    public AttachService(AttachRepository attachRepository){
        this.attachRepository = attachRepository;  
    }
    
    public void uploadAttachList(MyBoard board, List<MultipartFile> fileList) throws IOException{
        MyBoardAttachFileHandler fileHandler;
        List<MyAttach> myAttachList = new ArrayList<>();
        // 디스크에 저장
        fileHandler = MyBoardAttachFileHandler.builder()
                                              .board(board).build();

        for(MultipartFile file : fileList) {
            fileHandler.saveFile(file);
            
            // Test code
            // fileHandler.saveFileTest(file);

            MyAttach attach = fileHandler.getLastMyAttach();
            attach.setBoardNo(board.getBoardNo());
            myAttachList.add(attach);
        }                                             
        
        // DB에 저장
        attachRepository.insertAll(myAttachList);
    }

    public List<MyAttach> getAttachList(Long boardNo){
        return attachRepository.selectAttacheList(boardNo);
    }
}
