package com.example.goodluck.service.board;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.domain.AttachRepository;
import com.example.goodluck.domain.MyAttach;
import com.example.goodluck.global.FilePathHelper;
import com.example.goodluck.global.SaveType;
import com.example.goodluck.service.LocalFileStorageService;

@Service
public class AttachService {
    
    private final AttachRepository attachRepository;
    private final LocalFileStorageService fileService;

    public AttachService(AttachRepository attachRepository, LocalFileStorageService fileService){
        this.attachRepository = attachRepository;  
        this.fileService = fileService;
    }
    
    /*
     * 첨부파일 저장 메서드
     */
    public void upload(Long boardNo, List<MultipartFile> images){
        List<MyAttach> attaches = new ArrayList<>();

        // 서버에 저장
        for(MultipartFile image:images){
            String relatvieFileName = fileService.save(image, boardNo, SaveType.BOARD);
            // split extension, name, path
            String filePath = FilePathHelper.getDirectoryPath(relatvieFileName);
            String fileName = FilePathHelper.getFileNameOlny(filePath) + FilePathHelper.getExtension(filePath);
            Long size = image.getSize();
            attaches.add(MyAttach.builder()
                            .boardNo(boardNo)
                            .fileName(fileName)
                            .filePath(filePath)
                            .fileSize(size)
                            .build());
        }                                    
        
        // DB에 저장
        attachRepository.saveAll(attaches);
    }

    /*
     * 첨부파일 조회 메서드
     */
    public List<MyAttach> getAttachList(Long boardNo){
        return attachRepository.findByBoardNo(boardNo);
    }

    /*
     * 첨부파일 삭제 메서드
     */
    public void remove(List<MyAttach> attaches){

        for(MyAttach attach : attaches){
            String relativcePathName = attach.getFilePath() + attach.getFileName();
            fileService.delete(relativcePathName);
        }

    }

    public void removeByBoardNo(Long boardNo){
        List<MyAttach> attaches = getAttachList(boardNo);
        remove(attaches);
    }    
}
