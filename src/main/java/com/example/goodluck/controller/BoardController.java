package com.example.goodluck.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.goodluck.domain.MyBoard;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.global.helper.LoginSessionHelper;
import com.example.goodluck.service.board.BoardService;
import com.example.goodluck.service.board.dto.BoardWriteRequest;
import com.example.goodluck.service.user.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class BoardController {
    
    private final BoardService boardService;
    private final UserService userService;

    public BoardController(BoardService boardService, UserService userService){
        this.boardService = boardService;
        this.userService = userService;
    }
    
    /*
     * 게시글 목록 조회
     */
    
    @GetMapping("/list/{page}")
    public String getBoardList(@PathVariable(name="page", required = false) Long page, Model model){
        if(page == null || page == 0 ){
            page = 1L;
        }
        List<MyBoard> result = boardService.findAll(page);
        List<Integer> pages = boardService.getPageNumbers();
        
        model.addAttribute("boards", result);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", pages.size());
        model.addAttribute("startPage", pages.get(0));
        model.addAttribute("endPage", pages.get(pages.size() - 1));
        
        return "board/list";
    }

    @GetMapping("/list")
    public String redirectToFirstPage() {
        return "redirect:/list/1";
    }

    /*
     * 게시글 상세 조회
     */
    @GetMapping("/board/{boardNo}")
    public String getBoardDetail(@PathVariable("boardNo") Long boardNo, Model model){
        MyBoard result = boardService.findBoardByNo(boardNo);

        model.addAttribute("board", result);

        return "board/board";
    }

    /*
     * 게시글 작성 처리
     */
    @GetMapping("/board/write")
    public String getBoardWriteForm(HttpSession session, Model model) {
        if(LoginSessionHelper.isValidate()) return "redirect:/";
        
        model.addAttribute("requestData", new BoardWriteRequest());
        return "board/write";
    }
    
    @PostMapping("/board/write")
    public String postBoardWrite(
        HttpSession session,
        @ModelAttribute(name="boardWriteDto") @Valid BoardWriteRequest param,
        @RequestParam(name="fileImage",required = false) List<MultipartFile> files) 
    {
        if(!LoginSessionHelper.isValidate()) return "redirect:/";
        /* 작성자 정보 */
        Long userNo = LoginSessionHelper.getUserNo();
        MyUser writer = userService.getUser(userNo);

        Long boardNo = boardService.write(writer, param, files);

        return "redirect:/board/" + boardNo;
    }
    
    /*
     * 게시글 수정 처리
     */
    

    /*
     * 게시글 삭제 요청
     */
    @DeleteMapping("/board/{boardNo}")
    public String postBoardDelete( @PathVariable("boardNo") Long boardNo) {
            
        if(!LoginSessionHelper.isValidate()) return "redirect:/";
        /* 작성자 정보 */
        Long userNo = LoginSessionHelper.getUserNo();
        MyUser writer = userService.getUser(userNo);
        MyBoard board = boardService.findBoardByNo(boardNo);
        if(writer.getUserNo().equals(board.getWriter().getUserNo())){
            boardService.delete(boardNo);
        }

        return "redirect:/list?page=1";
    }
    
}
