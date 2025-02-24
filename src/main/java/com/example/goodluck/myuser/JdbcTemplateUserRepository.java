package com.example.goodluck.myuser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.NonNull;

import com.example.goodluck.domain.MyUser;

public class JdbcTemplateUserRepository implements UserRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateUserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    //INSERT
    @Override
    public MyUser insertNew(MyUser newMyUser) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("MY_USER");
        
        newMyUser.setUserNo(generateUserNoKey());
        
        Map<String,Object> map = new HashMap<>();
        map.put("USER_NO",  newMyUser.getUserNo());
        map.put("USER_NAME",newMyUser.getUserName());
        map.put("USER_ID",  newMyUser.getUserId());
        map.put("USER_PW",  newMyUser.getUserPw());
        map.put("TEL_NO",   newMyUser.getTelNo());
        map.put("USER_EMAIL",newMyUser.getUserEmail());
        map.put("POST_NO",  newMyUser.getPostNo());
        map.put("ADDRESS_MAIN",newMyUser.getAddressMain());
        map.put("ADDRESS_DETAIL",newMyUser.getAddressDetail());
        map.put("PROFILE_IMG_PATH", newMyUser.getProfileImgPath());
        map.put("PROFILE_IMG_NAME", newMyUser.getProfileImgName());
        
        jdbcInsert.execute(new MapSqlParameterSource(map));
        return newMyUser;
    }
    // SELECT 
    @Override
    public Optional<MyUser> selectById(String id) {
        List<MyUser> result = jdbcTemplate.query("select * from MY_USER where USER_ID = ?", userRowMapper(), id);
        return result.stream().findAny();
    }
    @Override
    public Optional<MyUser> selectByIdPw(String id, String pw) {
        List<MyUser> result = jdbcTemplate.query("select * from MY_USER where USER_ID = ? AND USER_PW = ?", userRowMapper(), id, pw );
        return result.stream().findAny();
    }
    @Override
    public Optional<MyUser> selectByNo(Long no) {
        List<MyUser> result = jdbcTemplate.query("select * from MY_USER where USER_NO = ?", userRowMapper(), no);
        return result.stream().findAny();
    }
    @Override
    public List<MyUser> selectAll() {
        return jdbcTemplate.query("select * from MY_USER", userRowMapper());
    }
    @Override
    public Optional<MyUser> selectByIdEmail(String id, String email) {
        List<MyUser> result = jdbcTemplate.query("select * from MY_USER where USER_ID = ? AND USER_EMAIL = ?", userRowMapper(), id, email);
        return result.stream().findAny();
    }
    // DELETE 
    @Override
    public Optional<Long> deleteByNo(Long no) {
        int deletedRow = jdbcTemplate.update("delete from MY_User WHERE USER_NO = ?", no);
        // if (deletedRow == 0) {
        //     throw new InvalidUserNoException("User with no " + no + " not found.");
        // }
        return Optional.of(no);
    }
    // UPDATE
    @Override
    public Optional<MyUser> updateUser(MyUser user) {
        String query = """
                        UPDATE MY_USER 
                        SET USER_NAME = ?, 
                            USER_PW = ?, 
                            TEL_NO = ?, 
                            POST_NO = ?, 
                            ADDRESS_MAIN =?, 
                            ADDRESS_DETAIL = ?,
                            PROFILE_IMG_PATH = ?,
                            PROFILE_IMG_NAME = ? 
                            WHERE USER_NO = ?
                        """;
        int updatedRow =  jdbcTemplate.update(query, 
                                            user.getUserName(), 
                                            user.getUserPw(), 
                                            user.getTelNo(), 
                                            user.getPostNo(), 
                                            user.getAddressMain(), 
                                            user.getAddressDetail(),
                                            user.getProfileImgPath(),
                                            user.getProfileImgName(), 
                                            user.getUserNo());
        // if (updatedRow == 0){
        //     throw new InvalidUserNoException("User not found.");
        // }                    
        return Optional.of(user);
    }

    // private 
    private Long generateUserNoKey() {
        String sequenceQuery = "SELECT MY_USER_SEQ.NEXTVAL FROM DUAL";
        return jdbcTemplate.queryForObject(sequenceQuery, Long.class);        
    }

    private RowMapper<MyUser> userRowMapper(){
        return new RowMapper<MyUser> (){
            @Override
            public MyUser mapRow(@NonNull ResultSet rs, 
                                 int rowNum) throws SQLException {
                MyUser user = new MyUser();
                user.setUserNo(rs.getLong("USER_NO"));
                user.setUserName(rs.getString("USER_NAME"));
                user.setUserEmail(rs.getString("USER_EMAIL"));
                user.setUserId(rs.getString("USER_ID"));
                user.setUserPw(rs.getString("USER_PW"));
                user.setPostNo(rs.getString("POST_NO"));
                user.setAddressMain(rs.getString("ADDRESS_MAIN"));
                user.setAddressDetail(rs.getString("ADDRESS_DETAIL"));
                user.setTelNo(rs.getString("TEL_NO"));
                user.setProfileImgPath(rs.getString("PROFILE_IMG_PATH"));
                user.setProfileImgName(rs.getString("PROFILE_IMG_NAME"));
                
                return user;
            }
        };
    }
}
