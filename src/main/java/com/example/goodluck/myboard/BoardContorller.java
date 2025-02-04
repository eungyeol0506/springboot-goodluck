package com.example.goodluck.myboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.goodluck.domain.Myboard;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class BoardContorller {
    
    private BoardService boardService;

    @Autowired
    public BoardContorller(BoardService boardService){
        this.boardService = boardService;
    }
    
    @GetMapping("list")
    public String getBoardList(Model model){
        List<Myboard> result = boardService.getBoardList(1);
        model.addAttribute("boards", result);
        return "myboard/board_list";
    }
}
