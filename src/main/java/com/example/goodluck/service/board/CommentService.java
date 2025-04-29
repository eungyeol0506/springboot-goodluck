package com.example.goodluck.service.board;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.goodluck.domain.JdbcTemplateCommentRepository;
import com.example.goodluck.domain.MyComment;

@Service
public class CommentService {
    
    private final JdbcTemplateCommentRepository commentRepository;

    public CommentService(JdbcTemplateCommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }
    /**
     * 댓글 조회 
     */
    public List<MyComment> findByBoardNo(Long boardNo){
        if(boardNo == null || boardNo <= 0){
            throw new IllegalArgumentException("board 번호가 유효하지 않음");
        }

        return commentRepository.findByBoardNo(boardNo);
    }

    public List<MyComment> findByUserNo(Long userNo){
        if(userNo == null || userNo <= 0){
            throw new IllegalArgumentException("user 번호가 유효하지 않음");
        }

        return commentRepository.findByBoardNo(userNo);
    }

    /**
     * 댓글 작성 메서드
     */
    public void save(MyComment comment){
        if(comment.getBoardNo() == null || comment.getUserNo() == null){
            throw new IllegalArgumentException("잘못 생성된 comment 객체 저장 시도");
        }
        comment.setCreateDate(LocalDate.now());
        commentRepository.save(comment);
    }

    /**
     * 게시글 삭제 시 댓글 삭제 메서드
     */
    public void removeByBoardNo(Long boardNo){
        List<Long> commentNos = new ArrayList<>();

        List<MyComment> comments = commentRepository.findByBoardNo(boardNo);
        for (MyComment comment: comments){
            commentNos.add(comment.getCommentNo());
        }
        if(commentNos.isEmpty()){
            // 삭제 대상이 없음
            return;
        }
        commentRepository.remove(commentNos);
    }
}
