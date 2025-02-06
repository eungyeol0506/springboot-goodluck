package com.example.goodluck.myboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.goodluck.domain.MyBoard;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class BoardContorller {
    
    private BoardService boardService;

    @Autowired
    public BoardContorller(BoardService boardService){
        this.boardService = boardService;
    }
    
    @GetMapping("/list")
    public String getBoardList(@RequestParam("page") Long page, Model model){
        List<MyBoard> result = boardService.getBoardList(page);
        List<Integer> pages = boardService.getPageNumbers();

        model.addAttribute("boards", result);
        model.addAttribute("pages", pages);
        return "myboard/board_list";
    }

    @GetMapping("/board/{boardNo}")
    public String getBoardDetail(@PathVariable("boardNo") Long boardNo, Model model){
        MyBoard result = boardService.getBoardDetail(boardNo);
        model.addAttribute("board", result);

        return "myboard/board";
    }

    // 글작성 폼
    // 작성한 글 등록
}
