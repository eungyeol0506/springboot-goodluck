package com.example.goodluck.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateCommentRepository {
    // 도메인에서 설정한 table, sequence 이름 정보
    private final String COMMENT_TABLE = MyComment.CommentConstants.TABLE_NAME.getValue();
    private final String COMMENT_SEQUENCE = MyComment.CommentConstants.SEQUENCE_NAME.getValue();
    private final String KEY_COLUMN = MyComment.CommentConstants.PRIVATE_KEY.getValue();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateCommentRepository(final DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<MyComment> findByBoardNo(Long boardNo){
        String query = String.format("SELECT * FROM %s WHERE BOARD_NO = :boardNo", COMMENT_TABLE);

        return jdbcTemplate.query(query, Map.of("boardNo", boardNo), commentMapper());
    }
    public List<MyComment> findByUserNo(Long userNo){
        String query = String.format("SELECT * FROM %s WHERE USER_NO = :userNo", COMMENT_TABLE);

        return jdbcTemplate.query(query, Map.of("userNo", userNo), commentMapper());
    }
    public void save(MyComment comment){
        String query = String.format("INSERT INTO %s (COMMENT_NO, USER_NO, BOARD_NO, REPLY, CREATE_DATE) " +
                                    "VALUES ( %s.NEXTVAL, :userNo, :boardNo, :reply, :createDate)",
                                    COMMENT_TABLE, COMMENT_SEQUENCE);

        Map<String, Object> parameters = Map.of(
            "boardNo", comment.getBoardNo(),
            "userNo", comment.getUserNo(),
            "reply", comment.getReply(),
            "createDate", comment.getCreateDate()
        );

        jdbcTemplate.update(query, parameters);

    }

    public void remove(List<Long> comments){
        // query
        String query = String.format(
            "DELETE FROM %s WHERE %s IN (:commentList)",
            COMMENT_TABLE, KEY_COLUMN);

        // parameter
        Map<String, Object> parameters = Map.of("commentList", comments);

        // excute query
        jdbcTemplate.update(query, parameters);
    }

    private RowMapper<MyComment> commentMapper(){
        return new RowMapper<MyComment>(){

            @Override
            @Nullable
            public MyComment mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyComment comment = MyComment.builder()
                                        .commentNo(rs.getLong("COMMENT_NO"))
                                        .boardNo(rs.getLong("BOARD_NO"))
                                        .userNo(rs.getLong("USER_NO"))
                                        .createDate(rs.getDate("CREATE_DATE").toLocalDate())
                                        .reply(rs.getString("REPLY"))
                                            .build();
            
                return comment;
            }
        };
    }
}
