package com.example.goodluck.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

// @Repository
public class JdbcTemplateAttachRepository implements AttachRepository{

    private final String ATTACH_TABLE = MyAttach.AttachConstants.TABLE_NAME.getValue();
    private final String ATTACH_SEQUENCE = MyAttach.AttachConstants.SEQUENCE_NAME.getValue();
    private final String KEY_COLUMN = MyAttach.AttachConstants.PRIVATE_KEY.getValue();

    private final NamedParameterJdbcTemplate jdbcTemplate;
    
    public JdbcTemplateAttachRepository(DataSource dataSource){
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    /*
     * 저장 메서드
     */
    @Override
    public void saveAll(List<MyAttach> files) {
        String query = String.format("INSERT INTO %s (ATTACH_NO, FILE_NAME, FILE_PATH, FILE_SIZE, BOARD_NO) " +
                                     " VALUES (%s.NEXTVAL, :fileName, :filePath, :fileSize, :boardNo)",
                                     ATTACH_TABLE, ATTACH_SEQUENCE);

        // 쿼리 실행
        jdbcTemplate.batchUpdate(query, SqlParameterSourceUtils.createBatch(files));
    }

    /*
     * 조회 메서드
     */
    @Override
    public List<MyAttach> findByBoardNo(Long boardNo) {
        String query = String.format("SELECT * FROM %s WHERE %s = :boardNo",
                                    ATTACH_TABLE, "BOARD_NO");

        return jdbcTemplate.query(query, Map.of("boardNo", boardNo), attachRowMapper());
    }

    /*
     * 삭제 메서드
     */
    @Override
    public void remove(List<Long> attaches) {
        String query = String.format("DELETE FROM %s WHERE %s IN (:attachNos)",
                                    ATTACH_TABLE, KEY_COLUMN);
        
        jdbcTemplate.update(query, Map.of("attachNos", attaches));
    }


    private RowMapper<MyAttach> attachRowMapper(){
        return new RowMapper<MyAttach>(){
            @Override
            public MyAttach mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyAttach attach = MyAttach.builder()
                                        .attachNo(rs.getLong("ATTACH_NO")) 
                                        .fileName(rs.getString("FILE_NAME"))
                                        .filePath(rs.getString("FILE_PATH"))
                                        .fileSize(rs.getLong("FILE_SIZE"))
                                        .boardNo(rs.getLong("BOARD_NO"))
                                        .build()
                                        ;

                return attach;
            }
            
        };   
    }

}
