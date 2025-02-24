package com.example.goodluck.myuser;

import javax.sql.DataSource;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.goodluck.domain.MyUser;
import com.example.goodluck.myuser.JdbcTemplateUserRepository;

// @JdbcTest
@SpringBootTest
// @Transactional
public class JdbcTemplateUserRepositoryTest {

    private final JdbcTemplateUserRepository userRepository;

    @Autowired
    public JdbcTemplateUserRepositoryTest(DataSource dataSource) {
        this.userRepository = new JdbcTemplateUserRepository(dataSource);
    }

    @Test
    @DisplayName("회원이 잘 저장되는지 확인")
    void saveUser(){
        // given
        MyUser newUser = MyUser.creatDummy(0L);
        // when
        MyUser saveUser = userRepository.insertNew(newUser);
        // then
        Assertions.assertThat(newUser).isEqualTo(saveUser);
    }

    
    @Test
    @DisplayName("회원이 잘 저장되고 각각 파라미터로 조회를 하는지 확인")
    void findBy(){
        //given 테스트데이터가 주어졌을 때
        MyUser testUser = userRepository.insertNew(getTestNewUser());
        //when 올바른 입력 시 
        MyUser result1 = userRepository.selectByNo(testUser.getUserNo()).get();
        MyUser result2 = userRepository.selectById(testUser.getUserId()).get();
        MyUser result3 = userRepository.selectByIdPw(testUser.getUserId(), testUser.getUserPw()).get();
        // 왜 오류남?? 검색 
        // MyUser result4 = userRepository.selectByIdEmail(testUser.getUserId(), testUser.getUserEmail()).orElse(null);
        //then 데이터를 갖고와야 함함
        Assertions.assertThat(testUser.getUserNo()).isEqualTo(result1.getUserNo());
        Assertions.assertThat(testUser.getUserNo()).isEqualTo(result2.getUserNo());
        Assertions.assertThat(testUser.getUserNo()).isEqualTo(result3.getUserNo());
        // Assertions.assertThat(testUser.getUserNo()).isEqualTo(result4.getUserNo());
    }

    
    @Test
    @DisplayName("회원 데이터가 잘 삭제되는지 확인")
    void deleteUser(){
        // given
        MyUser testUser = userRepository.insertNew(getTestNewUser());
        // when 삭제하는 경우
        Long delUserNo = userRepository.deleteByNo(testUser.getUserNo()).get();
        // then 테스트 데이터가 삭제된 경우랑 같은지 확인
        Assertions.assertThat(testUser.getUserNo()).isEqualTo(delUserNo);
    }

    @Test
    @DisplayName("회원 정보를 수정할 수 있는지 확인")
    void updateUser(){
        //given 기존 정보
        MyUser user = userRepository.insertNew(getTestNewUser());
        String newName = "테스터-2";
        user.setUserName(newName);
        //when update 시
        MyUser resultUser = userRepository.updateUser(user).get();
        MyUser resultUser2 = userRepository.selectById(resultUser.getUserId()).get();
        //then 없으면 실패, 있으면 잘 수정되었는지 확인
        Assertions.assertThat(resultUser.getUserName()).isEqualTo(newName);
        Assertions.assertThat(resultUser.getUserName()).isEqualTo(resultUser2.getUserName()); 
    }

    private MyUser getTestNewUser() {
        MyUser newUser = new MyUser();
        newUser.setUserId("test");
        newUser.setUserPw("test");
        newUser.setUserName("테스터");
        newUser.setUserEmail("test@testest.com");
        return newUser;
    }
}
