package com.example.goodluck.service.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardModifyRequest  {
    private Long boardNo;
    @NotBlank(message = "제목은 필수값입니다.")
    private String boardTitle;
    @NotBlank(message = "내용은 필수값입니다.")
    private String contents;


}
