package com.example.goodluck.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

// @Repository
public class JdbcTemplateUserRepository implements UserRepository{

    private final String USER_TABLE = MyUser.UserConstants.TABLE_NAME.getValue();
    private final String USER_SEQUENCE = MyUser.UserConstants.SEQUENCE_NAME.getValue();
    private final String KEY_COLUMN = MyUser.UserConstants.PRIVATE_KEY.getValue();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateUserRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    
    /**
     * 저장 메서드
     */
    @Override
    public void save(MyUser user) {
        String query = String.format("INSERT INTO %s (USER_NO, USER_NAME, USER_ID, USER_PW, USER_EMAIL, " + 
                                     "            POST_NO, ADDRESS_MAIN, ADDRESS_DETAIL, TEL_NO, PROFILE_IMG_PATH, PROFILE_IMG_NAME)" +
                                     " VALUES(%s.NEXTVAL, :userName, :userId, :userPw, :userEmail, " +
                                     "            :postNo, :addressMain, :addressDetail, :telNo, :profileImgPath, :profileImgName)",
                                     USER_TABLE, USER_SEQUENCE);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userName", user.getUserName());
        parameters.put("userId", user.getUserId());
        parameters.put("userPw", user.getUserPw());
        parameters.put("userEmail", user.getUserEmail());
        parameters.put("postNo", user.getPostNo());
        parameters.put("addressMain", user.getAddressMain());
        parameters.put("addressDetail", user.getAddressDetail());
        parameters.put("telNo", user.getTelNo());
        parameters.put("profileImgPath", user.getProfileImgPath());
        parameters.put("profileImgName", user.getProfileImgName());
                
        jdbcTemplate.update(query, parameters);
    }

    /*
     * 조회 메서드
     */
    @Override
    public Optional<MyUser> findById(String id) {
        String query = String.format("SELECT * FROM %s WHERE %s = :userId",
                                    USER_TABLE, "USER_ID");

        return jdbcTemplate.query(query, Map.of("userId", id), userRowMapper())
                           .stream()
                           .findAny();
    }

    @Override
    public Optional<MyUser> findByNo(Long no) {
        String query = String.format("SELECT * FROM %s WHERE %s = :userNo",
                                    USER_TABLE, KEY_COLUMN);

        return jdbcTemplate.query(query, Map.of("userNo", no), userRowMapper())
                           .stream()
                           .findAny();
    }

    /*
     * 삭제 메서드 (= 회원 탈퇴)
     */
    
    @Override
    public void remove(Long no) {
        String query = String.format("DELETE FROM %s WHERE %s = :userNo",
                                    USER_TABLE, KEY_COLUMN);
        
        Map<String, Object> parameters = Map.of("userNo", no);
        
        jdbcTemplate.update(query, parameters);
    }
    
    /*
     * 업데이트 메서드
     */

    @Override
    public void update(MyUser user) {
        String query = String.format("UPDATE %s " +
                                     "SET USER_NAME = :userName, TEL_NO = :telNo, POST_NO = :postNo, " +
                                     " ADDRESS_MAIN = :addressMain, ADDRESS_DETAIL = :addressDetail, " +
                                     " PROFILE_IMG_PATH = :profileImgPath, PROFILE_IMG_NAME = :profileImgName " +
                                     "WHERE %s = :userNo",
                                      USER_TABLE, KEY_COLUMN);
        
        Map<String,Object> parameters = Map.of(
            "userName", user.getUserName(),
            "telNo", user.getTelNo(),
            "postNo", user.getPostNo(),
            "addressMain", user.getAddressMain(),
            "addressDetail", user.getAddressDetail(),
            "profileImgPath", user.getProfileImgPath(),
            "profileImgName", user.getProfileImgName(),
            "userNo", user.getUserNo()
        );

        jdbcTemplate.update(query, parameters);
    }

    

    private RowMapper<MyUser> userRowMapper(){

        return new RowMapper<MyUser> (){
            @Override
            public MyUser mapRow(ResultSet rs, 
                                 int rowNum) throws SQLException {
                MyUser user = MyUser.builder()
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
                            .build();
                
                return user;
            }
        };
    }

}
