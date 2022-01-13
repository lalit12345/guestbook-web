package com.guestbook.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

import com.guestbook.entity.Entry;
import com.guestbook.entity.User;
import com.guestbook.exception.BusinessException;
import com.guestbook.model.Constants;
import com.guestbook.model.GuestEntry;
import com.guestbook.model.GuestEntryUpdateDto;
import com.guestbook.model.RegistrationDto;
import com.guestbook.repository.EntryRepository;
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

	@Mock
	private EntryRepository entryRepository;

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
	public void shouldThrowExceptionWhenUserNotFoundOnAddingEntry() {

		User user = User.builder().emailId("test@test.com").fullName("Test Test").mobileNumber("1231231231")
				.role("USER").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.empty());

		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			userService.addEntry("test@test.com", "sample1", "image1");
		});

		verify(userRepository, times(0)).save(user);
		assertEquals(businessException.getMessage(), "User not found");
	}

	@Test
	public void shouldAddNewEntry() {

		User user = User.builder().emailId("test@test.com").fullName("Test Test").mobileNumber("1231231231")
				.role("USER").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));

		userService.addEntry("test@test.com", "sample1", "image1");

		Entry newEntry = Entry.builder().entryText("sample1").entryImage("image1").user(user).build();
		List<Entry> entries = new ArrayList<>();
		entries.add(newEntry);

		user.setEntries(entries);

		verify(userRepository, times(1)).save(user);
	}

	@Test
	public void shouldReturnListOfEntries() {

		User user = User.builder().emailId("test1@test1.com").fullName("Test1 Test1").mobileNumber("1231231231")
				.build();
		Entry entry = Entry.builder().entryText("sample").entryImage("image1").user(user)
				.updatedDate(LocalDateTime.now()).build();
		List<Entry> entries = new ArrayList<>();
		entries.add(entry);

		user.setEntries(entries);
		List<User> users = new ArrayList<>();
		users.add(user);

		when(userRepository.findAllByRole(anyString())).thenReturn(users);

		Map<String, List<GuestEntry>> actualEntries = userService.getListOfEntries();

		assertEquals(1, actualEntries.size());
		assertEquals("sample", actualEntries.get("test1@test1.com").get(0).getEntryText());
		assertEquals("image1", actualEntries.get("test1@test1.com").get(0).getEntryImage());
	}

	@Test
	public void shouldThrowExceptionWhenUserNotFound() {

		Entry entry = Entry.builder().entryText("sample").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.empty());

		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			userService.approve("test@test.com", "1");
		});

		verify(entryRepository, times(0)).save(entry);
		assertEquals(businessException.getMessage(), "User not found");
	}

	@Test
	public void shouldThrowExceptionWhenEntryNotFound() {

		User user = User.builder().emailId("test@test.com").fullName("Test Test").mobileNumber("1231231231").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));

		when(entryRepository.findById(1)).thenReturn(Optional.empty());

		Entry entry = Entry.builder().entryText("sample").build();

		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			userService.approve("test@test.com", "1");
		});

		verify(entryRepository, times(0)).save(entry);
		assertEquals(businessException.getMessage(), "Entry not found");
	}

	@Test
	public void shouldApproveTheEntry() {

		User user = User.builder().emailId("test@test.com").fullName("Test Test").mobileNumber("1231231231").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));

		Entry entry = Entry.builder().entryText("sample").entryImage("image1").isApproved(false).user(user).build();

		when(entryRepository.findById(anyInt())).thenReturn(Optional.of(entry));

		Entry approvedEntry = userService.approve("test@test.com", "1");

		assertEquals(approvedEntry.isApproved(), true);
	}

	@Test
	public void shouldThrowExceptionWhenUserNotFoundOnUpdate() {

		GuestEntryUpdateDto guestEntryUpdateDto = GuestEntryUpdateDto.builder().emailId("test@test.com").entryId("1")
				.entryText("sample2").build();

		Entry entry = Entry.builder().entryText("sample").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.empty());

		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			userService.update(guestEntryUpdateDto);
		});

		verify(entryRepository, times(0)).save(entry);
		assertEquals(businessException.getMessage(), "User not found");
	}

	@Test
	public void shouldThrowExceptionWhenEntryNotFoundOnUpdate() {

		GuestEntryUpdateDto guestEntryUpdateDto = GuestEntryUpdateDto.builder().emailId("test@test.com").entryId("1")
				.entryText("sample2").build();

		User user = User.builder().emailId("test@test.com").fullName("Test Test").mobileNumber("1231231231").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));

		when(entryRepository.findById(1)).thenReturn(Optional.empty());

		Entry entry = Entry.builder().entryText("sample").build();

		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			userService.update(guestEntryUpdateDto);
		});

		verify(entryRepository, times(0)).save(entry);
		assertEquals(businessException.getMessage(), "Entry not found");
	}

	@Test
	public void shouldUpdateTheUserDetails() {

		GuestEntryUpdateDto guestEntryUpdateDto = GuestEntryUpdateDto.builder().emailId("test@test.com").entryId("1")
				.entryText("sample2").build();

		User user = User.builder().emailId("test@test.com").fullName("Test Test").mobileNumber("1231231231").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));

		Entry entry = Entry.builder().entryText("sample").entryImage("image1").isApproved(false).user(user).build();

		when(entryRepository.findById(anyInt())).thenReturn(Optional.of(entry));

		Entry updatedEntry = userService.update(guestEntryUpdateDto);

		assertEquals(updatedEntry.getEntryText(), "sample2");
	}

	@Test
	public void shouldThrowExceptionWhenUserNotFoundOnDelete() {

		Entry entry = Entry.builder().entryText("sample").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.empty());

		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			userService.delete("test@test.com", "1");
		});

		verify(entryRepository, times(0)).save(entry);
		assertEquals(businessException.getMessage(), "User not found");
	}

	@Test
	public void shouldThrowExceptionWhenEntryNotFoundOnDelete() {

		User user = User.builder().emailId("test@test.com").fullName("Test Test").mobileNumber("1231231231").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));

		when(entryRepository.findById(1)).thenReturn(Optional.empty());

		Entry entry = Entry.builder().entryText("sample").build();

		BusinessException businessException = assertThrows(BusinessException.class, () -> {
			userService.delete("test@test.com", "1");
		});

		verify(entryRepository, times(0)).save(entry);
		assertEquals(businessException.getMessage(), "Entry not found");
	}

	@Test
	public void shouldMarkTheEntryDeleted() {

		User user = User.builder().emailId("test@test.com").fullName("Test Test").mobileNumber("1231231231").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));

		Entry entry = Entry.builder().entryText("sample").entryImage("image1").isApproved(false).user(user).build();

		when(entryRepository.findById(anyInt())).thenReturn(Optional.of(entry));

		Entry deletedEntry = userService.delete("test@test.com", "1");

		assertEquals(deletedEntry.isDeleteFlag(), true);
	}
}
