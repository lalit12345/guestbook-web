package com.guestbook.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
public class SecurityServiceImplTest {

	@Autowired
	private SecurityService securityService;

	@Test
	@WithMockUser(username = "test@test.com", password = "pwd", roles = "USER")
	public void shouldReturnTrueIfUserIsAuthenticated() {

		assertTrue(securityService.isAuthenticated());
	}

	@Test
	public void shouldReturnFalseIfUserIsNotAuthenticated() {

		assertFalse(securityService.isAuthenticated());
	}
}
