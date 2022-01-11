package com.guestbook.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.guestbook.model.Constants;
import com.guestbook.service.SecurityService;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SecurityService securityService;

	@Test
	public void shouldReturnLoginViewOnGet() throws Exception {

		mockMvc.perform(get(LoginController.PATH)).andExpect(status().isOk())
				.andExpect(view().name(LoginController.VIEW));
	}

	@Test
	public void shouldRedirectToLoginSuccessViewWhenUserAlresyAuthenticated() throws Exception {

		Mockito.when(securityService.isAuthenticated()).thenReturn(true);

		mockMvc.perform(get(LoginController.PATH)).andExpect(status().is3xxRedirection())
				.andExpect(view().name(Constants.REDIRECT + LoginSuccessController.PATH));
	}
}
