package com.example.goodluck.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyAttach {
    // pk
    private Long attachNo;

    private String fileName;
    private String filePath;
    private Long fileSize;

    private Long boardNo;


    @RequiredArgsConstructor
    public enum AttachConstants{
        PRIVATE_KEY("ATTACH_NO"),
        TABLE_NAME("MY_ATTACH"),
        SEQUENCE_NAME("MY_ATTACH_SEQ");

        private final String value;
        public String getValue(){
            return value;
        }
    }

    public String getAttachFullPath(){
        return "/uploads/attaches/" + boardNo + "/" + fileName;
    }
}
