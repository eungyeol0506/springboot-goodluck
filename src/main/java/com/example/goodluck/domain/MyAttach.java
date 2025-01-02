package com.example.goodluck.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyAttach {
    private long attachNo;
    private String fileName;
    private String filePath;
    private int fileSize;
    private Myboard board;
}
