package com.guestbook.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.guestbook.model.Constants;

@SpringBootTest
@AutoConfigureMockMvc
public class LandingPageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldRedirectToWelcomeViewOnGet() throws Exception {

		mockMvc.perform(get("/")).andExpect(status().is3xxRedirection())
				.andExpect(view().name(Constants.REDIRECT + WelcomeController.PATH));
	}
}
