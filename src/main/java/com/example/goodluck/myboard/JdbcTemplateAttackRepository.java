package com.example.goodluck.myboard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.example.goodluck.domain.MyAttach;
public class JdbcTemplateAttackRepository implements AttachRepository{

    private final JdbcTemplate jdbcTemplate;
    
    public JdbcTemplateAttackRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insertAll(List<MyAttach> files) throws DataAccessException{

        long attachNo = getAttachNo();

        for(MyAttach file : files){
            file.setNewAttachNo(attachNo++);
        }

        // batch update
        String ATTACH_SQL = "INSERT INTO my_attach( attach_no, file_name, file_path, file_size, board_no) values(?, ?, ?, ? ,?)";
        jdbcTemplate.batchUpdate(ATTACH_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException{
                MyAttach file = files.get(i);
                ps.setLong(1, file.getAttachNo());
                ps.setString(2, file.getFileName());
                ps.setString(3, file.getFilePath());
                ps.setLong(4, file.getFileSize());
                ps.setLong(5, file.getBoardNo());
            }

            @Override
            public int getBatchSize() {
                return files.size();
            }
        }
        );
    }

    private long getAttachNo() {
        return jdbcTemplate.queryForObject("select MY_ATTACH_SEQ.NEXTVAL from DUAL", Long.class);
    }

    @Override
    public List<MyAttach> selectAttacheList(Long boardNo) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select MY_ATTACH.* ");
        queryBuilder.append(" from MY_ATTACH");
        queryBuilder.append(" left join MY_BOARD");
        queryBuilder.append(" on MY_ATTACH.BOARD_NO = MY_BOARD.BOARD_NO");
        queryBuilder.append(" where MY_BOARD.BOARD_NO = " + boardNo);

        List<MyAttach> result = jdbcTemplate.query(queryBuilder.toString(), attachRowMapper());
        return result;
    }

    @Override
    public int updateAttach(MyAttach attach) {
        // TODO Auto-generated method stub
        return 0;
    }

    private RowMapper<MyAttach> attachRowMapper(){
        return new RowMapper<MyAttach>(){
            @SuppressWarnings("null")
            @Override
            public MyAttach mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyAttach attach = MyAttach.builder()
                                        .attachNo(rs.getLong("ATTACH_NO")) 
                                        .fileName(rs.getString("FILE_NAME"))
                                        .filePath(rs.getString("FILE_PATH"))
                                        .fileSize(rs.getInt("FILE_SIZE"))
                                        .build()
                                        ;

                return attach;
            }
            
        };   
    }
    @Override
    public int deleteAttach(MyAttach attach) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int deleteAttachList(List<MyAttach> attachList) {
        // TODO Auto-generated method stub
        return 0;
    }
}
