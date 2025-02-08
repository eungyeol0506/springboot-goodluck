package com.example.goodluck.myboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.goodluck.domain.MyBoard;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.myboard.dto.BoardWriteRequestDto;
import com.example.goodluck.myuser.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
public class BoardContorller {
    
    private BoardService boardService;
    private UserService userService;

    @Autowired
    public BoardContorller(BoardService boardService, UserService userService){
        this.boardService = boardService;
        this.userService = userService;
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

    // 게시글 작성 폼
    @GetMapping("/board/write")
    public String getBoardWriteForm(HttpSession session, Model model) {
        if (session.getAttribute("userNo") == null){
            model.addAttribute("notice", "로그인 세션 정보가 없습니다.");
            return "home";
        }
        model.addAttribute("preValue", new MyBoard());
        return "myboard/newboard_form";
    }
    
    // 게시글 등록
    @PostMapping("/board/write")
    public String postMethodName(
        HttpSession session,
        @ModelAttribute(name="boardWriteDto") @Valid BoardWriteRequestDto boardWriteRequest) {
        // 사용자 정보 찾기    
        Long userNo = (Long) session.getAttribute("userNo");
        MyUser writer = userService.getUserInfo(userNo).orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없음"));
        // 게시글 정보 
        MyBoard newBoard = boardWriteRequest.toDomain();
        newBoard.setWriterInfo(writer);

        MyBoard result = boardService.writeBoard(newBoard);

        return "redirect:/board/" + result.getBoardNo() ;
    }
    
    // 게시글 수정 폼
    @GetMapping("/board/edit/{boardNo}")
    public String getMethodName(@PathVariable("boardNo") Long boardNo, Model model) {

        return "myboard/board_form";
    }
    
    // 게시글 수정하기
    @PostMapping("/board/edit/{boardNo}")
    public String postMethodName()
        // @ModelAttribute(name="boardEditRequest") BoardEditRequest boardEditRequest) 
    {
        
        return "redirect:/board/" + 1L;
    }
    
}
