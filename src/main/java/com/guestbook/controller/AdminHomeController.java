package com.guestbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guestbook.model.Constants;
import com.guestbook.service.SecurityService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(AdminHomeController.PATH)
public class AdminHomeController {

	@Autowired
	private SecurityService securityService;

	public static final String VIEW = "admin-home";

	public static final String PATH = Constants.PATH_DELIMITER + VIEW;

	@GetMapping
	public String get(HttpSession httpSession, Authentication authentication) {

		log.info("GET {}", PATH);

		if (!securityService.isAuthenticated()) {
			return Constants.REDIRECT + LoginController.PATH;
		}

		return VIEW;
	}
}
