package com.guestbook.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.guestbook.entity.Entry;
import com.guestbook.entity.User;

@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void shouldFindUserByEmailId() {

		User user = User.builder().emailId("test@test.com").password("pwd").fullName("Test Test")
				.mobileNumber("1231231231").role("USER").build();

		Entry entry = Entry.builder().entryText("sample").entryImage("image1").user(user).build();
		List<Entry> entries = new ArrayList<>();
		entries.add(entry);

		user.setEntries(entries);

		userRepository.save(user);

		Optional<User> optionalUser = userRepository.findByEmailId("test@test.com");

		assertTrue(optionalUser.isPresent());

		User savedUser = optionalUser.get();

		assertNotNull(savedUser);
		assertEquals(savedUser.getEmailId(), "test@test.com");
		assertEquals(savedUser.getFullName(), "Test Test");
		assertEquals(savedUser.getMobileNumber(), "1231231231");
		assertEquals(savedUser.getEntries().get(0).getEntryText(), "sample");
		assertEquals(savedUser.getEntries().get(0).isApproved(), false);
		assertEquals(savedUser.getEntries().get(0).isDeleteFlag(), false);
	}

	@Test
	public void shouldReturnListOfUsersWithRoleUSERAndNotMarkedAsDeleted() {

		User user1 = User.builder().emailId("test1@test1.com").password("pwd").fullName("Test1 Test1")
				.mobileNumber("1111111111").role("USER").build();
		Entry entry1 = Entry.builder().entryText("sample1").entryImage("image1").user(user1).build();
		List<Entry> entries1 = new ArrayList<>();
		entries1.add(entry1);
		userRepository.save(user1);

		User user2 = User.builder().emailId("test2@test2.com").password("pwd").fullName("Test2 Test2")
				.mobileNumber("2222222222").role("USER").build();
		Entry entry2 = Entry.builder().entryText("sample2").entryImage("image2").user(user2).build();
		List<Entry> entries2 = new ArrayList<>();
		entries2.add(entry2);
		userRepository.save(user2);

		User user3 = User.builder().emailId("test3@test3.com").password("pwd").fullName("Test3 Test3")
				.mobileNumber("3333333333").role("USER").build();
		Entry entry3 = Entry.builder().entryText("sample3").entryImage("image3").user(user3).build();
		List<Entry> entries3 = new ArrayList<>();
		entries3.add(entry3);
		userRepository.save(user3);

		List<User> users = userRepository.findAllByRole("USER");

		assertEquals(users.size(), 3);
	}
}
