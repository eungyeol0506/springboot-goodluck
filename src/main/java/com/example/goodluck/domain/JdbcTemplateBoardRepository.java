package com.example.goodluck.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

// @Repository
public class JdbcTemplateBoardRepository implements BoardRepository{
    
    private final String BOARD_TABLE = MyBoard.BoardConstants.TABLE_NAME.getValue();
    private final String BOARD_SEQUENCE = MyBoard.BoardConstants.SEQUENCE_NAME.getValue();
    private final String KEY_COLUMN = MyBoard.BoardConstants.PRIVATE_KEY.getValue();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateBoardRepository(final DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    
    /*
     * 조회 메서드 - user 랑 함께 불러옴
     */
    @Override
    public List<MyBoard> findAll(Long start, Long end) {
        String query = """
                        SELECT *
                          FROM ( SELECT B.BOARD_NO, B.BOARD_TITLE, B.CONTENTS, B.CREATE_DATE,
                                        B.UPDATE_DATE, B.VIEW_CNT, M.*, 
                                        ROW_NUMBER() OVER(ORDER BY B.BOARD_NO DESC) AS RNUM
                                   FROM MY_BOARD B
                                   LEFT JOIN MY_USER M ON B.USER_NO = M.USER_NO
                                )
                         WHERE RNUM BETWEEN :start AND :end
                       """;
        Map<String, Object> parameters = Map.of(
            "start", start,
            "end", end 
        );
        
        return jdbcTemplate.query(query, parameters, boardRowMapper());
    }

    @Override
    public Optional<MyBoard> findByNo(Long boardNo) {
        // query
        String query = """
                        SELECT MY_BOARD.*, MY_USER.* FROM MY_BOARD 
                         LEFT JOIN MY_USER
                         ON MY_BOARD.USER_NO = MY_USER.USER_NO
                         WHERE MY_BOARD.BOARD_NO = :boardNo
                       """;

        return jdbcTemplate.query(query, Map.of("boardNo", boardNo), boardRowMapper())
                            .stream().findAny();
    }

    @Override
    public Long getAllCount() {
        String query = String.format("SELECT COUNT(*) FROM %s", BOARD_TABLE);
        return jdbcTemplate.queryForObject(query, new HashMap<>(), Long.class);
    }

    /*
     * 삭제 메서드
     */
    @Override
    public void remove(Long boardNo) {
        String query = String.format("DELETE FROM %s WHERE %s = :boardNo",
                                    BOARD_TABLE, KEY_COLUMN);
    
        jdbcTemplate.update(query, Map.of("boardNo", boardNo));    
    }
    
    /*
    * 저장 메서드
    */
    @Override
    public void save(MyBoard newBoard) {
        String query = String.format("INSERT INTO %s(BOARD_NO, BOARD_TITLE, CONTENTS, CREATE_DATE, UPDATE_DATE, VIEW_CNT, USER_NO) "+
                                    " VALUES ( %s.NEXTVAL, :title, :contents, :createDate, :updateDate, :viewCnt, :userNo)",
                                    BOARD_TABLE, BOARD_SEQUENCE);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", newBoard.getBoardTitle());
        parameters.put("contents", newBoard.getContents());
        parameters.put("createDate", newBoard.getCreateDate());
        parameters.put("updateDate", newBoard.getUpdateDate());
        parameters.put("viewCnt", newBoard.getViewCnt());
        parameters.put("userNo", newBoard.getWriter().getUserNo());
        
        
        jdbcTemplate.update(query, parameters);
    }
    
    /*
    * 업데이트 메서드
    */
    @Override
    public void update(MyBoard board) {
        String query = String.format("UPDATE %s SET " +
                                     "BOARD_TITLE = :title, CONTENTS = :contents, UPDATE_DATE = :updateDate, VIEW_CNT = :viewCnt " +
                                     "WHERE %s = :boardNo ",
                                     BOARD_TABLE, KEY_COLUMN);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", board.getBoardTitle());
        parameters.put("contents", board.getContents());
        parameters.put("updateDate", board.getUpdateDate());
        parameters.put("viewCnt", board.getViewCnt());
        parameters.put("boardNo", board.getBoardNo());

        jdbcTemplate.update(query, parameters);
    }
    
    
    private RowMapper<MyBoard> boardRowMapper(){
        return new RowMapper<MyBoard>(){
            @Override
            public MyBoard mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyBoard board = MyBoard.builder()
                                    .boardNo(rs.getLong("BOARD_NO"))
                                    .viewCnt(rs.getInt("VIEW_CNT"))
                                    .boardTitle(rs.getString("BOARD_TITLE"))
                                    .contents(rs.getString("CONTENTS"))
                                    .createDate(rs.getDate("CREATE_DATE") != null ? rs.getDate("CREATE_DATE").toLocalDate() : null)
                                    .updateDate(rs.getDate("UPDATE_DATE") != null ? rs.getDate("UPDATE_DATE").toLocalDate() : null)
                                    .writer(MyUser.builder()
                                            .userNo(rs.getLong("USER_NO"))
                                            .userName(rs.getString("USER_NAME"))
                                            .userEmail(rs.getString("USER_EMAIL"))
                                            .userId(rs.getString("USER_ID"))
                                            .userPw(rs.getString("USER_PW"))
                                            .postNo(rs.getString("POST_NO"))
                                            .telNo(rs.getString("TEL_NO"))
                                            .addressMain(rs.getString("ADDRESS_MAIN"))
                                            .addressDetail(rs.getString("ADDRESS_DETAIL"))
                                            .profileImgPath(rs.getString("PROFILE_IMG_PATH"))
                                            .profileImgName(rs.getString("PROFILE_IMG_NAME"))
                                            .build()
                                        )
                                .build();
                return board;
            }
            
        };   
    }

}
