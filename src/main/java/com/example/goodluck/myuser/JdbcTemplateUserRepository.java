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

import com.example.goodluck.domain.MyUser;

public class JdbcTemplateUserRepository implements UserRepository{

    private final JdbcTemplate jdbcTemplate;
    // private JdbcTemplate jdbcTemplate;

    public JdbcTemplateUserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Long> deleteByNo(Long no) {
        int deletedRow = jdbcTemplate.update("delete from MY_User WHERE USER_NO = ?", no);
        if(deletedRow > 0){
            return Optional.of(no);
        }
        return Optional.empty();
    }

    @Override
    public List<MyUser> selectAll() {
        return jdbcTemplate.query("select * from MY_USER", userRowMapper());
    }

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
    public MyUser insertNew(MyUser newMyUser) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("MY_USER");
        // .usingGeneratedKeyColumns("USER_NO");
        
        newMyUser.setUserNo(generatedUserNoKey());
        Map<String, Object> parameters = toMap(newMyUser);
        jdbcInsert.execute(new MapSqlParameterSource(parameters));
        // Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        // newMyUser.setUserNo(key.longValue());
        return newMyUser;
    }
    
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
        jdbcTemplate.update(query, 
                            user.getUserName(), 
                            user.getUserPw(), 
                            user.getTelNo(), 
                            user.getPostNo(), 
                            user.getAddressMain(), 
                            user.getAddressDetail(),
                            user.getProfileImgPath(),
                            user.getProfileImgName(), 
                            user.getUserNo());
                            
        return Optional.ofNullable(user);
    }
    
    @Override
    public Optional<MyUser> selectByIdEmail(String id, String email) {
        List<MyUser> result = jdbcTemplate.query("select * from MY_USER where USER_ID = ? AND USER_EMAIL = ?", userRowMapper(), id, email);
        return result.stream().findAny();
    }

    
    private Long generatedUserNoKey() {
        String sequenceQuery = "SELECT MY_USER_SEQ.NEXTVAL FROM DUAL";
        return jdbcTemplate.queryForObject(sequenceQuery, Long.class);        
    }
    private Map<String,Object> toMap(MyUser user){
        Map<String,Object> map = new HashMap<>();
        map.put("USER_NO",  user.getUserNo());
        map.put("USER_NAME",user.getUserName());
        map.put("USER_ID",  user.getUserId());
        map.put("USER_PW",  user.getUserPw());
        map.put("TEL_NO",   user.getTelNo());
        map.put("USER_MAIL",user.getUserEmail());
        map.put("POST_NO",  user.getPostNo());
        map.put("ADDRESS_MAIN",user.getAddressMain());
        map.put("ADDRESS_DETAIL",user.getAddressDetail());
        map.put("PROFILE_IMG_PATH", user.getProfileImgPath());
        map.put("PROFILE_IMG_NAME", user.getProfileImgName());

        return map;
    }
    private RowMapper<MyUser> userRowMapper(){
        return new RowMapper<MyUser> (){
            @Override
            public MyUser mapRow(ResultSet rs, int rowNum) throws SQLException {
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
                user.setProfileImgPath("PROFILE_IMG_PATH");
                user.setProfileImgName("PROFILE_IMG_NAME");
                
                return user;
            }
        };
    }
}
