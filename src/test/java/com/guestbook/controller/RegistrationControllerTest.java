package com.guestbook.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.guestbook.model.Constants;
import com.guestbook.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Test
	public void shouldReturnViewOnGet() throws Exception {

		mockMvc.perform(get(RegistrationController.PATH)).andExpect(status().isOk())
				.andExpect(view().name(RegistrationController.VIEW));
	}

	@Test
	public void shouldRegisterUserAndRedirectToLoginOnPost() throws Exception {

		doNothing().when(userService).registeruser(any(), any());

		MockHttpServletRequestBuilder builder = post(RegistrationController.PATH).param("emailId", "test@test.com")
				.param("password", "pwd").param("fullName", "Test Test").param("mobileNumber", "1231231231");

		mockMvc.perform(builder).andExpect(status().is3xxRedirection())
				.andExpect(view().name(Constants.REDIRECT + LoginController.PATH));
	}

	@Test
	public void shouldNotRegisterUserIfAlredyExists() throws Exception {

		doNothing().when(userService).registeruser(any(), any());

		MockHttpServletRequestBuilder builder = post(RegistrationController.PATH).param("emailId", "test@test.com")
				.param("password", "pwd").param("fullName", "Test Test").param("mobileNumber", "1231231231")
				.flashAttr("userAlreadyExists", "User already exists with given emailId");

		mockMvc.perform(builder).andExpect(status().isOk()).andExpect(view().name(RegistrationController.VIEW))
				.andExpect(model().attribute("userAlreadyExists", "User already exists with given emailId"));
	}
}
