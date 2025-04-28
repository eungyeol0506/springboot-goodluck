package com.example.goodluck.myboard;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.goodluck.domain.MyBoard;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.exception.myboard.BoardAttachUploadException;
import com.example.goodluck.exception.myboard.ForbiddenBoardAccessException;
import com.example.goodluck.exception.myuser.UserNotFoundException;
import com.example.goodluck.myboard.dto.BoardModifyRequestDto;
import com.example.goodluck.myboard.dto.BoardWriteRequestDto;
import com.example.goodluck.myuser.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class BoardContorller {
    
    private BoardService boardService;
    private UserService userService;
    private AttachService attachService;

    @Autowired
    public BoardContorller(BoardService boardService, UserService userService, AttachService attachService){
        this.boardService = boardService;
        this.userService = userService;
        this.attachService = attachService;
    }
    
    // 게시글 목록
    @GetMapping("/list")
    public String getBoardList(@RequestParam(name="page", defaultValue="1") Long page, Model model){
        List<MyBoard> result = boardService.getBoardList(page);
        List<Integer> pages = boardService.getPageNumbers();

        model.addAttribute("boards", result);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", pages.size());
        model.addAttribute("startPage", pages.get(0));
        model.addAttribute("endPage", pages.get(pages.size() - 1));

        // model.addAttribute("pages", pages);
        return "myboard/board_list";
    }

    // 게시글 상세보기
    @GetMapping("/board/{boardNo}")
    public String getBoardDetail(@PathVariable("boardNo") Long boardNo, Model model){
        MyBoard result = boardService.getBoardDetail(boardNo);
        result.setAttachList(attachService.getAttachList(boardNo));

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
    
    // 게시글 등록 요청
    @PostMapping("/board/write")
    public String postBoardWrite(
        HttpSession session,
        @ModelAttribute(name="boardWriteDto") @Valid BoardWriteRequestDto boardWriteRequest,
        @RequestParam(name="fileImage",required = false) List<MultipartFile> multipartFiles) {
        // 사용자 정보 찾기    
        Long userNo = (Long) session.getAttribute("userNo");
        if (userNo == null){
            throw new UserNotFoundException("세션정보를 찾을 수 없습니다.");
        }
        MyUser writer = userService.getUserInfo(userNo).orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없음"));
        // 게시글 정보 
        MyBoard newBoard = boardWriteRequest.toDomain();
        newBoard.setWriterInfo(writer);

        MyBoard result = boardService.writeBoard(newBoard);

        if(multipartFiles != null){
            // 첨부파일 저장
            try{
                attachService.uploadAttachList(newBoard, multipartFiles);
            }catch(IOException exception){
                throw new BoardAttachUploadException();
            }
        }

        return "redirect:/board/" + result.getBoardNo() ;
    }
    
    // 게시글 수정 폼
    @PostMapping("/board/form")
    public String postBoardEditForm(
        Model model,
        @ModelAttribute(name="boardNo") Long boardNo,
        HttpSession session) 
    {
        Long userNo = (Long) session.getAttribute("userNo");
        MyBoard board = boardService.getBoardDetail(boardNo);
        if (! userNo.equals(board.getUser().getUserNo())){
            throw new ForbiddenBoardAccessException("수정 권한이 없는 사용자입니다.", board);
        }
        
        board.setAttachList(attachService.getAttachList(boardNo));

        model.addAttribute("preValue", board);

        return "myboard/board_form";
    }
    
    // 게시글 수정 요청
    @PostMapping("/board/form/{boardNo}")
    public String postBoardEdit(
        @ModelAttribute(name="boardEditRequest") BoardModifyRequestDto boardModifyRequest,
        @RequestParam(name="fileImage",required = false) List<MultipartFile> multipartFiles)
    {
        MyBoard board = boardModifyRequest.toDomain();
        
        boardService.eidtBoard(board);
        return "redirect:/board/" + board.getBoardNo();
    }
    
    @PostMapping("/board/delete/{boardNo}")
    public String postBoardDelete(
        @PathVariable("boardNo") Long boardNo, 
        HttpSession session) {
            
        Long userNo = (Long) session.getAttribute("userNo");
        // MyUser user = userService.getUserInfo(userNo).get();
        // get board No
        MyBoard board = boardService.getBoardDetail(boardNo);

        if (! userNo.equals(board.getUser().getUserNo())){
            throw new ForbiddenBoardAccessException("삭제 권한이 없는 사용자입니다.", board);
        }
        boardService.deleteBoard(boardNo);

        return "redirect:/list?page=1";
    }
    
}
