package com.example.goodluck.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class JdbcTemplateUserRepositoryTest {
	
	@Autowired JdbcTemplateUserRepository userRepository;
	@Test
	@DisplayName("저장 후 조회에 성공")
	void successSave(){
		// given
		MyUser user = getTestUser();
		
		// when
		userRepository.save(user);

		// then
		MyUser result = userRepository.findById(user.getUserId()).get();
		assertNotNull(result.getUserNo(), "값이 정상적으로 저장됨");
		assertEquals(user.getUserId(), result.getUserId(), "저장한 값을 잘 불러옴");
		
	}
	
	@Test
	@DisplayName("사용자 정보 수정 성공")
	void successUpdate(){
		// given
		MyUser user = getTestUser();
		userRepository.save(user);
		
		MyUser thatUser = userRepository.findById(user.getUserId()).get();
		thatUser.setUserName("새로운 이름");
		
		// when
		userRepository.update(thatUser);

		// then
		MyUser result = userRepository.findById(user.getUserId()).get();
		assertEquals("새로운 이름", result.getUserName(), "이름이 변경되었음");
	}

	@Test
	@DisplayName("사용자 삭제 성공")
	void successRemove(){
		// given
		MyUser user = getTestUser();
		userRepository.save(user);
		
		MyUser thatUser = userRepository.findById(user.getUserId()).get();

		// when
		userRepository.remove(thatUser.getUserNo());

		// then
		MyUser result = userRepository.findById(user.getUserId()).orElse(null);
		assertNull(result, "삭제되어 조회가 되지 않아야 함");
	}

	private MyUser getTestUser() {
		return MyUser.builder()
					.userName("테스트이름")
					.userId("1234@!")
					.userPw("12341234")
					.userEmail("test@email.com")
					.build();
	}
}
