package com.example.goodluck.domain;

import lombok.Data;

@Data
public class MyAttach {
    private long attachNo;
    private String fileName;
    private String filePath;
    private int fileSize;
    private Myboard board;
}
