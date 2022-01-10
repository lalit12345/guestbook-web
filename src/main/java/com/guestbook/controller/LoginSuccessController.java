package com.guestbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guestbook.model.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(LoginSuccessController.PATH)
public class LoginSuccessController {

	public static final String PATH = Constants.PATH_DELIMITER + "success";

	@GetMapping
	public String get(HttpSession httpSession, Authentication authentication) {

		log.info("GET {}", PATH);

		String role = authentication.getAuthorities().toString();

		if (role.contains(Constants.ADMIN_ROLE)) {
			return Constants.REDIRECT + AdminHomeController.PATH;
		}

		return Constants.REDIRECT + UserHomeController.PATH;
	}
}
