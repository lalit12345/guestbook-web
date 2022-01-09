package com.guestbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guestbook.model.Constants;
import com.guestbook.model.Credentials;
import com.guestbook.model.MessageEnum;
import com.guestbook.service.SecurityService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(LoginController.PATH)
public class LoginController {

	@Autowired
	private SecurityService securityService;

	public static final String CREDENTIALS_ATTRIBUTE = "credentials";

	public static final String VIEW = "login";

	public static final String PATH = Constants.PATH_DELIMITER + VIEW;

	@GetMapping
	public String get(Model model, String error, String logout, HttpSession httpSession) {

		log.info("GET {}", PATH);

		if (securityService.isAuthenticated()) {
			return Constants.REDIRECT + HomeController.PATH;
		}

		if (error != null)
			model.addAttribute(Constants.ERROR_ATTRIBUTE, MessageEnum.INVALID_LOGIN_CREDENTIALS_MESSAGE.value());

		if (logout != null)
			model.addAttribute(Constants.MESSAGE_ATTRIBUTE, MessageEnum.LOGOUT_MESSAGE.value());

		model.addAttribute(CREDENTIALS_ATTRIBUTE, Credentials.builder().build());

		return VIEW;
	}
}
