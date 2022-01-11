package com.guestbook.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.guestbook.entity.User;
import com.guestbook.model.Constants;
import com.guestbook.model.GuestEntry;
import com.guestbook.service.SecurityService;
import com.guestbook.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminHomeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SecurityService securityService;

	@MockBean
	private UserService userService;

	@Test
	public void shouldReturnAdminHomeViewOnGetAndUserIsAuthenticated() throws Exception {

		when(securityService.isAuthenticated()).thenReturn(true);

		mockMvc.perform(get(AdminHomeController.PATH)).andExpect(status().isOk())
				.andExpect(view().name(AdminHomeController.VIEW));
	}

	@Test
	public void shouldReturnAdminLoginViewOnGetAndUserIsNotAuthenticated() throws Exception {

		when(securityService.isAuthenticated()).thenReturn(false);

		mockMvc.perform(get(AdminHomeController.PATH)).andExpect(status().is3xxRedirection())
				.andExpect(view().name(Constants.REDIRECT + LoginController.PATH));
	}

	@Test
	public void shouldReturnAdminLoginViewOnGetViewAndUserIsNotAuthenticated() throws Exception {

		when(securityService.isAuthenticated()).thenReturn(false);

		mockMvc.perform(get(AdminHomeController.VIEW_PATH)).andExpect(status().is3xxRedirection())
				.andExpect(view().name(Constants.REDIRECT + Constants.PATH_DELIMITER + LoginController.VIEW));
	}

	@Test
	public void shouldReturnAdminHomeViewWithEntries() throws Exception {

		when(securityService.isAuthenticated()).thenReturn(true);

		List<GuestEntry> entries = new ArrayList<>();
		entries.add(
				GuestEntry.builder().emailId("test@test.com").fullName("test test").mobileNumber("1231231231").build());

		when(userService.getListOfEntries()).thenReturn(entries);

		mockMvc.perform(get(AdminHomeController.VIEW_PATH)).andExpect(status().isOk())
				.andExpect(view().name(AdminHomeController.VIEW)).andExpect(model().attribute("entries", entries));
	}

	@Test
	public void shouldReturnAdminHomeViewWithNoEntries() throws Exception {

		when(securityService.isAuthenticated()).thenReturn(true);

		when(userService.getListOfEntries()).thenReturn(Collections.emptyList());

		mockMvc.perform(get(AdminHomeController.VIEW_PATH)).andExpect(status().isOk())
				.andExpect(view().name(AdminHomeController.VIEW)).andExpect(model().attribute("emptyList", true));
	}

	@Test
	public void shouldApproveEntryForGivenEmailId() throws Exception {

		when(securityService.isAuthenticated()).thenReturn(true);

		when(userService.approve(anyString())).thenReturn(User.builder().emailId("test@test.com").build());

		List<GuestEntry> entries = new ArrayList<>();
		entries.add(
				GuestEntry.builder().emailId("test@test.com").fullName("test test").mobileNumber("1231231231").build());

		when(userService.getListOfEntries()).thenReturn(entries);

		mockMvc.perform(post("/approve/{userId}", "test@test.com")).andExpect(status().isOk())
				.andExpect(view().name(AdminHomeController.VIEW)).andExpect(model().attribute("entries", entries));
	}

	@Test
	public void shouldUpdateEntryForEntryDetails() throws Exception {

		when(securityService.isAuthenticated()).thenReturn(true);

		when(userService.update(any())).thenReturn(User.builder().emailId("test@test.com").build());

		List<GuestEntry> entries = new ArrayList<>();
		entries.add(GuestEntry.builder().emailId("test@test.com").fullName("test1 test1").mobileNumber("1231231231")
				.build());

		when(userService.getListOfEntries()).thenReturn(entries);

		MockHttpServletRequestBuilder builder = post("/update").param("emailId", "test@test.com")
				.param("fullName", "Test1 Test1").param("mobileNumber", "1231231231");

		mockMvc.perform(builder).andExpect(status().isOk()).andExpect(view().name(AdminHomeController.VIEW))
				.andExpect(model().attribute("entries", entries));
	}

	@Test
	public void shouldDeleteEntryForGivenEmailId() throws Exception {

		when(securityService.isAuthenticated()).thenReturn(true);

		when(userService.delete(anyString())).thenReturn(User.builder().emailId("test@test.com").build());

		List<GuestEntry> entries = new ArrayList<>();
		entries.add(
				GuestEntry.builder().emailId("test@test.com").fullName("test test").mobileNumber("1231231231").build());

		when(userService.getListOfEntries()).thenReturn(entries);

		mockMvc.perform(get("/delete/{userId}", "test@test.com")).andExpect(status().isOk())
				.andExpect(view().name(AdminHomeController.VIEW)).andExpect(model().attribute("entries", entries));
	}
}
