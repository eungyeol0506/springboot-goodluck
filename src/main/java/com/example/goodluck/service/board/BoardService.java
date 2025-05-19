package com.example.goodluck.service.board;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.goodluck.domain.BoardRepository;
import com.example.goodluck.domain.MyAttach;
import com.example.goodluck.domain.MyBoard;
import com.example.goodluck.domain.MyComment;
import com.example.goodluck.domain.MyUser;
import com.example.goodluck.service.board.dto.BoardModifyRequest;
import com.example.goodluck.service.board.dto.BoardWriteRequest;

@Service
@Transactional
public class BoardService {
    // 한 페이지당 리스트 수
    private final static int LIST_SIZE = 15;
    private final BoardRepository boardRepository;
    private final BoardConvertor boardConvertor;
    private final AttachService attachService;
    private final CommentService commentService;

    public BoardService(BoardRepository boardRepository, BoardConvertor boardConvertor, AttachService attachService, CommentService commentService){
        this.boardRepository = boardRepository;
        this.boardConvertor = boardConvertor;
        this.attachService = attachService;
        this.commentService = commentService;
    }
    
    /*
     * 게시글 작성 메서드
     */
    public Long write(MyUser writer, BoardWriteRequest param, List<MultipartFile> images){
        if(writer == null || param == null){
            throw new IllegalArgumentException();
        }

        LocalDate now = LocalDate.now();
        MyBoard board = boardConvertor.toDomain(param);
        board.setCreateDate(now);
        board.setWriter(writer);
        board.setViewCnt(0);
        
        Long boardNo = boardRepository.save(board);
        board.setBoardNo(boardNo);

        if(images != null && !images.isEmpty()){
            attachService.upload(boardNo, images);
        }

        return boardNo;
    }

    /*
     * 게시글 조회 메서드
     */
    public MyBoard findBoardByNo(Long boardNo){
        MyBoard board = boardRepository.findByNo(boardNo).orElseThrow(
            () -> new BoardServiceException(BoardError.BOARD_NOT_FOUND)
        );
        
        board.setViewCnt(board.getViewCnt()+1);
        boardRepository.update(board);

        board.setComments(commentService.findByBoardNo(boardNo));
        board.setAttaches(attachService.getAttachList(boardNo));

        return board;
    }

    /*
     * 게시글 목록 조회 메서드
     */
    public List<MyBoard> findAll(Long page){
        long start = ((page-1) * LIST_SIZE) + 1;
        long end = page * LIST_SIZE;
        return boardRepository.findAll(start, end);
    }

    /*
     * 게시글 수정 메서드
     */
    public Long modify(BoardModifyRequest param, List<Long> deleteImages, List<MultipartFile> images){
        LocalDate now = LocalDate.now();
        MyBoard board = boardConvertor.toDomain(param);
        board.setUpdateDate(now);

        // 삭제가 필요한 경우 첨부파일 삭제
        if(deleteImages != null && !deleteImages.isEmpty()){
            List<MyAttach> attaches = attachService.getAttachList(board.getBoardNo());
            List<MyAttach> deleteAttachList = getDeleteAttachList(attaches, deleteImages);
            attachService.remove(deleteAttachList);
        }
        // 새 이미지 등록
        if(images != null && !images.isEmpty()){
            attachService.upload(board.getBoardNo(), images);
        }
        // 게시글 수정
        boardRepository.update(board);
        return board.getBoardNo();
    }

    /*
     * Get list of MyAttach objects that need to be deleted
     */
    private List<MyAttach> getDeleteAttachList(List<MyAttach> attaches, List<Long> deleteImages) {
        List<MyAttach> deleteAttachList = new ArrayList<>();
        for (MyAttach attach : attaches) {
            if (deleteImages.contains(attach.getAttachNo())) {
                deleteAttachList.add(attach);
            }
        }
        return deleteAttachList;
    }

    /*
     * 게시글 삭제 메서드
     */
    public void delete(Long boardNo){
        commentService.removeByBoardNo(boardNo);
        attachService.removeByBoardNo(boardNo);

        boardRepository.remove(boardNo);
    }

    /*
     * 페이지 번호 조회 메서드
     */
    public List<Integer> getPageNumbers(){
        Long totalCnt = boardRepository.getAllCount();

        List<Integer> pageList = new ArrayList<>();
        // int idx = 1;
        long cnt = totalCnt;
        while(cnt > LIST_SIZE){
            pageList.add(pageList.size()+1);
            cnt -= LIST_SIZE;
        }
        if(cnt >= 1){
            pageList.add(pageList.size()+1);
        }

        return pageList; 
    }
    /*
     * 댓글 작성 메서드
     */
    public void addComment(Long userNo, Long boardNo, String reply){
        LocalDate now = LocalDate.now();
        MyComment comment = MyComment.builder()
                            .boardNo(boardNo)
                            .createDate(now)
                            .reply(reply)
                            .userNo(userNo)
                            .build();

        commentService.save(comment);
    }
}
