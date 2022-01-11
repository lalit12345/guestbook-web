package com.guestbook.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.guestbook.model.Constants;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginSuccessControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithMockUser(username = "test@test.com", password = "pwd", roles = "USER")
	public void shouldRedirectToUserHomeViewWhenRoleIsUser() throws Exception {

		mockMvc.perform(get(LoginSuccessController.PATH)).andExpect(status().is3xxRedirection())
				.andExpect(view().name(Constants.REDIRECT + UserHomeController.PATH));
	}

	@Test
	@WithMockUser(username = "admin@test.com", password = "pwd", roles = "ADMIN")
	public void shouldRedirectToAdminHomeViewWhenRoleIsUser() throws Exception {

		mockMvc.perform(get(LoginSuccessController.PATH)).andExpect(status().is3xxRedirection())
				.andExpect(view().name(Constants.REDIRECT + AdminHomeController.PATH));
	}
}
