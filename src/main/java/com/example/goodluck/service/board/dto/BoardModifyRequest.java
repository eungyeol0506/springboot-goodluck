package com.example.goodluck.service.board.dto;

import java.util.ArrayList;
import java.util.List;


import com.example.goodluck.domain.MyAttach;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardModifyRequest  {

    @NotNull(message = "게시글 번호는 필수값입니다.")
    private Long boardNo;

    @NotBlank(message = "제목은 필수값입니다.")
    private String boardTitle;
    @NotBlank(message = "내용은 필수값입니다.")
    private String contents;

    private List<SimpleMyAttach> attaches = new ArrayList<>();

    @Setter @Getter
    class SimpleMyAttach{
        Long attachNo;
        String attachPath;
    }

    public void addAttaches(MyAttach attach){
        SimpleMyAttach sAttach = new SimpleMyAttach();
        sAttach.setAttachNo(attach.getAttachNo());
        sAttach.setAttachPath(attach.getAttachFullPath());

        this.attaches.add(sAttach);
    }
}

