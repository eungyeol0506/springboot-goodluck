package com.example.goodluck.myboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.domain.MyBoard;

public class JdbcTemplateBoardRepository implements BoardRepository{
    
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateBoardRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Override
    public Optional<MyBoard> selectBoard(Long boardNo) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select MY_BOARD.*, MY_USER.*");
        queryBuilder.append(" from MY_BOARD");
        queryBuilder.append(" join MY_USER");
        queryBuilder.append(" on MY_BOARD.USER_NO = MY_USER.USER_NO");
        queryBuilder.append(" where MY_BOARD.BOARD_NO = " + boardNo);

        List<MyBoard> result = jdbcTemplate.query(queryBuilder.toString(), boardRowMapper());
        return result.stream().findAny();
    }
    
    @Override
    public List<MyBoard> selectBoardList(Long start, Long end) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select *");
        queryBuilder.append(" from (");
        queryBuilder.append("   select B.BOARD_NO, B.BOARD_TITLE, B.CONTENTS, B.CREATE_DATE, B.UPDATE_DATE, B.VIEW_CNT,");
        queryBuilder.append("   M.*, ROW_NUMBER() OVER(ORDER BY B.BOARD_NO DESC) as RNUM");
        queryBuilder.append("   from MY_BOARD B");
        queryBuilder.append("   left join MY_USER M");
        queryBuilder.append("   on B.USER_NO = M.USER_NO) ");
        queryBuilder.append(" where RNUM >= " + start + " AND RNUM <= " + end);

        return jdbcTemplate.query(queryBuilder.toString(), boardRowMapper());
    }
    
    @Override
    public MyBoard insertNew(MyBoard newBoard) throws DataAccessException{
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        
        Long boardNo = generateKey();
        newBoard.setBoardNo(boardNo);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("BOARD_NO", newBoard.getBoardNo());
        parameters.put("BOARD_TITLE", newBoard.getBoardTitle());
        parameters.put("CONTENTS", newBoard.getContents());
        parameters.put("UPDATE_DATE", null);
        parameters.put("CREATE_DATE", newBoard.getCreateDate());
        parameters.put("VIEW_CNT", 0);
        parameters.put("USER_NO", newBoard.getUser().getUserNo());
        
        jdbcInsert.withTableName("MY_BOARD");
        jdbcInsert.execute(new MapSqlParameterSource(parameters));

        return newBoard;
    }

    @Override
    public int updateBoard(MyBoard board) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update MY_BOARD");
        queryBuilder.append(" set BOARD_TITLE = '" + board.getBoardTitle() + "'");
        queryBuilder.append(", CONTENTS = '" + board.getContents() + "'");
        queryBuilder.append(", UPDATE_DATE = TO_DATE('"+ board.getUpdateDate().toString() +"')");
        queryBuilder.append(" where BOARD_NO = " + board.getBoardNo());

        int updatedRow = jdbcTemplate.update(queryBuilder.toString());
        return updatedRow;
    }
    
    @Override
    public int deleteBoard(Long boardNo) {
        int deletedRow = jdbcTemplate.update("delete from MY_BOARD where BOARD_NO = ?", boardNo);
        return deletedRow;
    }
    @Override
    public int updateBoardViewCnt(Long boardNo, int viewCnt) {
        String query ="";
        query += "update MY_BOARD ";
        query += "set view_cnt = " + String.valueOf(viewCnt);
        query += "where BOARD_NO =" + boardNo;

        return jdbcTemplate.update(query);
    }
    @Override
    public Long selectBoardCnt() {
        String query = "select count(*) from MY_BOARD";
        return jdbcTemplate.queryForObject(query, Long.class);
        
    }
    private Long generateKey() {
        return jdbcTemplate.queryForObject("select MY_BOARD_SEQ.NEXTVAL from DUAL", Long.class);        
    }
    private RowMapper<MyBoard> boardRowMapper(){
        return new RowMapper<MyBoard>(){
            @SuppressWarnings("null")
            @Override
            public MyBoard mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyBoard board = new MyBoard();
                board.setBoardNo(rs.getLong("BOARD_NO"));
                board.setViewCnt(rs.getInt("VIEW_CNT"));
                board.setBoardTitle(rs.getString("BOARD_TITLE"));
                board.setContents(rs.getString("CONTENTS"));
                board.setCreateDate(rs.getDate("CREATE_DATE") != null ? rs.getDate("CREATE_DATE").toLocalDate() : null);
                board.setUpdateDate(rs.getDate("UPDATE_DATE") != null ? rs.getDate("UPDATE_DATE").toLocalDate() : null);
                board.setUser(new MyUser(rs.getLong("USER_NO"), 
                                         rs.getString("USER_NAME"), 
                                         rs.getString("USER_EMAIL"), 
                                         rs.getString("USER_ID"), 
                                         rs.getString("USER_PW"), 
                                         rs.getString("POST_NO"), 
                                         rs.getString("ADDRESS_MAIN"), 
                                         rs.getString("ADDRESS_DETAIL"), 
                                         rs.getString("TEL_NO"), 
                                         rs.getString("PROFILE_IMG_PATH"), 
                                         rs.getString("PROFILE_IMG_NAME")
                                         ));
                return board;
            }
            
        };   
    }
}
