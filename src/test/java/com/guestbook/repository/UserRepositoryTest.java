package com.guestbook.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.guestbook.entity.User;

@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void shouldFindUserByEmailId() {

		userRepository.save(User.builder().emailId("test@test.com").password("pwd").fullName("Test Test")
				.mobileNumber("1231231231").entryText("sample").entryImage("image-path").role("USER").build());

		Optional<User> optionalUser = userRepository.findByEmailId("test@test.com");

		assertTrue(optionalUser.isPresent());

		User user = optionalUser.get();

		assertNotNull(user);
		assertEquals(user.getEmailId(), "test@test.com");
		assertEquals(user.getFullName(), "Test Test");
		assertEquals(user.getMobileNumber(), "1231231231");
		assertEquals(user.getEntryText(), "sample");
		assertEquals(user.isApproved(), false);
		assertEquals(user.isDeleteFlag(), false);
	}

	@Test
	public void shouldReturnListOfUsersWithRoleUSERAndNotMarkedAsDeleted() {

		User user1 = User.builder().emailId("test1@test1.com").password("pwd").fullName("Test1 Test1")
				.mobileNumber("1111111111").entryText("sample1").entryImage("image-path1").deleteFlag(false)
				.role("USER").build();
		userRepository.save(user1);

		User user2 = User.builder().emailId("test2@test2.com").password("pwd").fullName("Test2 Test2")
				.mobileNumber("2222222222").entryText("sample2").entryImage("image-path2").deleteFlag(true).role("USER")
				.build();
		userRepository.save(user2);

		User user3 = User.builder().emailId("test3@test3.com").password("pwd").fullName("Test3 Test3")
				.mobileNumber("3333333333").entryText("sample3").entryImage("image-path3").deleteFlag(false)
				.role("USER").build();
		userRepository.save(user3);

		List<User> users = userRepository.findAllByRoleAndDeleteFlag("USER", false);

		assertEquals(users.size(), 2);
		assertTrue(users.contains(user3));
		assertTrue(users.contains(user1));
	}
}
