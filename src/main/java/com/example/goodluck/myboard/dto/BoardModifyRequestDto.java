package com.example.goodluck.myboard.dto;

import com.example.goodluck.common.MyDto;
import com.example.goodluck.domain.MyBoard;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardModifyRequestDto implements MyDto<MyBoard> {
    @NotBlank(message = "제목은 필수값입니다.")
    private String boardTitle;
    @NotBlank(message = "내용은 필수값입니다.")
    private String contents;

    @Override
    public MyBoard toDomain() {
        return new MyBoard(0L, boardTitle, contents, 0, null, null, null);
    }
}
