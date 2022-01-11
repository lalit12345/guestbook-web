package com.guestbook.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.guestbook.entity.User;
import com.guestbook.model.Constants;
import com.guestbook.service.SecurityService;
import com.guestbook.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserHomeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SecurityService securityService;

	@MockBean
	private UserService userService;

	@Test
	public void shouldReturnUserHomeViewOnGetAndUserIsAuthenticated() throws Exception {

		when(securityService.isAuthenticated()).thenReturn(true);

		mockMvc.perform(get(UserHomeController.PATH)).andExpect(status().isOk())
				.andExpect(view().name(UserHomeController.VIEW));
	}

	@Test
	public void shouldRedirectToLoginViewOnGetAndUserIsNotAuthenticated() throws Exception {

		when(securityService.isAuthenticated()).thenReturn(false);

		mockMvc.perform(get(UserHomeController.PATH)).andExpect(status().is3xxRedirection())
				.andExpect(view().name(Constants.REDIRECT + LoginController.PATH));
	}

	@Test
	@WithMockUser(username = "test@test.com", password = "pwd", roles = "USER")
	public void shouldCreateAnEntryAndReturnView() throws Exception {

		when(userService.updateUser(anyString(), anyString(), anyString()))
				.thenReturn(User.builder().emailId("test@test.com").entryText("Thank you").build());

		MockHttpServletRequestBuilder builder = multipart(UserHomeController.PATH)
				.file(new MockMultipartFile("multipartFile", "sample.pdf", MediaType.APPLICATION_PDF_VALUE,
						"sample content".getBytes()))
				.param("entryText", "Thank you");

		mockMvc.perform(builder).andExpect(status().isOk()).andExpect(view().name(UserHomeController.VIEW))
				.andExpect(model().attribute("validEntry", "Entry is made successfully"));
	}

	@Test
	@WithMockUser(username = "test@test.com", password = "pwd", roles = "USER")
	public void shouldNotCreateAnEntryAndReturnViewWithInvalidData() throws Exception {

		when(userService.updateUser(anyString(), anyString(), anyString()))
				.thenReturn(User.builder().emailId("test@test.com").entryText("Thank you").build());

		MockHttpServletRequestBuilder builder = multipart(UserHomeController.PATH).file(new MockMultipartFile(
				"multipartFile", "", MediaType.APPLICATION_PDF_VALUE, "sample content".getBytes()));

		mockMvc.perform(builder).andExpect(status().isOk()).andExpect(view().name(UserHomeController.VIEW))
				.andExpect(model().attribute("invalidEntry", "Add atleast one entry"));
	}
}
