package com.guestbook.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.guestbook.entity.User;
import com.guestbook.repository.UserRepository;
import com.guestbook.service.impl.UserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

	@InjectMocks
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Mock
	private UserRepository userRepository;

	@BeforeEach
	public void init() {

		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void shouldReturnUserDetailsIfUserIsPresent() {

		User user = User.builder().emailId("test@test.com").password("pwd").fullName("Test Test")
				.mobileNumber("1231231231").isApproved(false).deleteFlag(false).role("USER").build();

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));

		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("test@test.com");

		assertEquals(userDetails.getUsername(), "test@test.com");
		assertEquals(userDetails.getPassword(), "pwd");
		assertTrue(userDetails.getAuthorities().toString().contains("USER"));
		assertEquals(userDetails.isAccountNonExpired(), true);
		assertEquals(userDetails.isAccountNonLocked(), true);
		assertEquals(userDetails.isCredentialsNonExpired(), true);
		assertEquals(userDetails.isEnabled(), true);
	}

	@Test
	public void shouldThrowUserNotFoundExceptionIfUserIsNotPresent() {

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.empty());

		UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class,
				() -> userDetailsServiceImpl.loadUserByUsername("test@test.com"));

		assertEquals(usernameNotFoundException.getMessage(), "User not found");
	}
}
