package com.example.goodluck.domain;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyComment {
    private long commentNo;
    private String reply;
    private Date createDate;
    private MyUser user;
    private Myboard board;
}
