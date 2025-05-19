package com.example.goodluck.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.goodluck.domain.MyAttach;
import com.example.goodluck.domain.MyBoard;
import com.example.goodluck.domain.MyComment;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.global.helper.LoginSessionHelper;
import com.example.goodluck.service.board.BoardService;
import com.example.goodluck.service.board.dto.BoardData;
import com.example.goodluck.service.board.dto.BoardModifyRequest;
import com.example.goodluck.service.board.dto.BoardWriteRequest;
import com.example.goodluck.service.board.dto.CommentRequest;
import com.example.goodluck.service.user.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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
        
        // 작성 권한 확인
        model.addAttribute("isWriter", false);
        if(LoginSessionHelper.isValidate()){
            Long currentUser = LoginSessionHelper.getUserNo();
            if ( currentUser.equals(result.getWriter().getUserNo())){
                model.addAttribute("isWriter", true);
            }
        }

        BoardData boardData = toBoardData(result);
        model.addAttribute("board", boardData);
        return "board/board";
    }

    /*
     * 게시글 작성 처리
     */
    @GetMapping("/board/write")
    public String getBoardWriteForm(HttpSession session, Model model) {
        if( !LoginSessionHelper.isValidate()) return "redirect:/";
        
        model.addAttribute("writeRequest", new BoardWriteRequest());
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
    @GetMapping("/board/modify/{boardNo}")
    public String getBoardModifyForm(@PathVariable("boardNo") Long boardNo, Model model) {
        if (!LoginSessionHelper.isValidate()) return "redirect:/";
        Long userNo = LoginSessionHelper.getUserNo();
        
        // Get current user and board
        MyUser currentUser = userService.getUser(userNo);
        MyBoard board = boardService.findBoardByNo(boardNo);

        // Check if current user is the writer
        if (!currentUser.getUserNo().equals(board.getWriter().getUserNo())) {
            return "redirect:/board/" + boardNo;
        }


        BoardModifyRequest requestData = toModifyRequest(board);

        model.addAttribute("modifyData", requestData);
        return "board/modify";
    }

    @PostMapping("/board/modify/{boardNo}")
    public String postBoardModify(
        @PathVariable("boardNo") Long boardNo,
        @ModelAttribute(name="modifyData") @Valid BoardModifyRequest param,
        @RequestParam(name="fileImage", required=false) List<MultipartFile> newFiles,
        @RequestParam(name="deleteImageNo", required=false) List<Long> deleteImageNoList
    ) {
        if (!LoginSessionHelper.isValidate()) return "redirect:/";
        Long userNo = LoginSessionHelper.getUserNo();

        // Get current user and board
        MyUser currentUser = userService.getUser(userNo);
        MyBoard board = boardService.findBoardByNo(boardNo);

        // Check if current user is the writer
        if (!currentUser.getUserNo().equals(board.getWriter().getUserNo())) {
            return "redirect:/board/" + boardNo;
        }

        boardService.modify(param, deleteImageNoList, newFiles);

        return "redirect:/board/" + boardNo;
    }

    /*
     * 게시글 삭제 요청
     */
    @PostMapping("/board/delete/{boardNo}")
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

    /*
     * 댓글 작성 메서드
     */
    @PostMapping("/board/{boardNo}/comment")
    public String postMethodName(
        @PathVariable("boardNo") Long boardNo,
        @RequestParam("reply") String reply) 
    {
        if(!LoginSessionHelper.isValidate()) return "redirect:/";
        Long userNo = LoginSessionHelper.getUserNo();

        boardService.addComment(userNo, boardNo, reply);

        return "redirect:/board/"+boardNo;
    }
    
    public BoardData toBoardData(MyBoard domain){
        BoardData boardData = new BoardData();
        boardData.setBoardNo(domain.getBoardNo());
        boardData.setBoardTitle(domain.getBoardTitle());
        boardData.setContents(domain.getContents());
        boardData.setViewCnt(domain.getViewCnt());
        if(domain.getUpdateDate() == null){
            boardData.setLastUpdateDate(domain.getCreateDate());
        }else{
            boardData.setLastUpdateDate(domain.getUpdateDate());
        }

        for (MyAttach image : domain.getAttaches()){
            boardData.getAttachPaths().add(image.getAttachFullPath());
        }

        for(MyComment comment : domain.getComments()){
            MyUser replyUser = userService.getUser(comment.getUserNo());
            boardData.addCommentData(replyUser.getUserName(), comment.getReply(), comment.getCreateDate());
        }

        return boardData;
    }
    public BoardModifyRequest toModifyRequest(MyBoard domain){
        BoardModifyRequest request = new BoardModifyRequest();
        request.setBoardNo(domain.getBoardNo());
        request.setBoardTitle(domain.getBoardTitle());
        request.setContents(domain.getContents());

        for(MyAttach image : domain.getAttaches()){
            request.addAttaches(image);
        }

        return request;
    }
}
