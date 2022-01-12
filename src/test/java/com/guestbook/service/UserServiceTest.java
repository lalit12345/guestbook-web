package com.guestbook.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.ExtendedModelMap;

import com.guestbook.entity.User;
import com.guestbook.exception.BusinessException;
import com.guestbook.model.Constants;
import com.guestbook.model.GuestEntry;
import com.guestbook.model.GuestEntryUpdateDto;
import com.guestbook.model.RegistrationDto;
import com.guestbook.repository.UserRepository;
import com.guestbook.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Mock
	private UserRepository userRepository;

	@BeforeEach
	public void init() {

		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void shouldReturnIfUserAlreadyRegistered() {

		User user = User.builder().emailId("test@test.com").build();

		ExtendedModelMap modelMap = new ExtendedModelMap();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));

		RegistrationDto registrationDto = RegistrationDto.builder().emailId("test@test.com").fullName("Test Test")
				.mobileNumber("1231231231").password("pwd").build();

		userService.registeruser(registrationDto, modelMap);

		verify(userRepository, times(0)).save(user);

		assertEquals("User already exists with given emailId", modelMap.getAttribute(Constants.USER_ALREADY_EXISTS));
	}

	@Test
	public void shouldRegisterUserWithGivenDetails() {

		User user = User.builder().emailId("test@test.com").fullName("Test Test").mobileNumber("1231231231")
				.password("encoded-password").role("USER").build();

		ExtendedModelMap modelMap = new ExtendedModelMap();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.empty());

		when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encoded-password");

		RegistrationDto registrationDto = RegistrationDto.builder().emailId("test@test.com").fullName("Test Test")
				.mobileNumber("1231231231").password("pwd").build();

		userService.registeruser(registrationDto, modelMap);

		verify(userRepository, times(1)).save(user);
	}

	@Test
	public void shouldThrowAnExceptionWhenUserDoesNotExists() {

		User user = User.builder().emailId("test@test.com").fullName("Test Test").mobileNumber("1231231231").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.empty());

		BusinessException businessException = assertThrows(BusinessException.class,
				() -> userService.updateUser("test@test.com", "sample", "file-path"));

		assertEquals("User not found", businessException.getMessage());
		verify(userRepository, times(0)).save(user);
	}

	@Test
	public void shouldUpdateUserWithEntryDetails() {

		User user = User.builder().emailId("test@test.com").fullName("Test Test").mobileNumber("1231231231").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));

		User updatedUser = userService.updateUser("test@test.com", "sample", "file-path");

		user.setEntryText("sample");
		user.setEntryImage("file-path");

		verify(userRepository, times(1)).save(user);
		assertEquals(updatedUser.getEntryText(), "sample");
		assertEquals(updatedUser.getEntryImage(), "file-path");
	}

	@Test
	public void shouldReturnListOfEntries() {

		User user1 = User.builder().emailId("test1@test1.com").fullName("Test1 Test1").mobileNumber("1231231231")
				.entryText("sample1").build();
		User user2 = User.builder().emailId("test2@test2.com").fullName("Test2 Test2").mobileNumber("3213213213")
				.entryText("sample2").build();

		List<User> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);

		when(userRepository.findAllByRoleAndDeleteFlag(anyString(), anyBoolean())).thenReturn(users);

		List<GuestEntry> entries = userService.getListOfEntries();

		assertEquals(2, entries.size());
	}

	@Test
	public void shouldApproveTheEntry() {

		User user = User.builder().emailId("test@test.com").fullName("Test Test").mobileNumber("1231231231")
				.isApproved(false).build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));

		User approvedUser = userService.approve("test@test.com");

		assertEquals(approvedUser.isApproved(), true);
	}

	@Test
	public void shouldUpdateTheUserDetails() {

		GuestEntryUpdateDto guestEntryUpdateDto = GuestEntryUpdateDto.builder().emailId("test@test.com")
				.mobileNumber("3213213213").fullName("Test1 Test1").entryText("sample2").build();

		User user = User.builder().emailId("test@test.com").fullName("Test Test").mobileNumber("1231231231")
				.entryText("sample1").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));

		User updatedUser = userService.update(guestEntryUpdateDto);

		assertEquals(updatedUser.getMobileNumber(), "3213213213");
		assertEquals(updatedUser.getFullName(), "Test1 Test1");
		assertEquals(updatedUser.getEntryText(), "sample2");
	}

	@Test
	public void shouldMarkTheEntryDeleted() {

		User user = User.builder().emailId("test@test.com").fullName("Test Test").mobileNumber("1231231231")
				.entryText("sample1").deleteFlag(false).build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));

		User deletedUser = userService.delete("test@test.com");

		assertEquals(deletedUser.isDeleteFlag(), true);
	}
}
