package com.guestbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guestbook.model.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(WelcomeController.PATH)
public class WelcomeController {

	public static final String VIEW = "welcome";

	public static final String PATH = Constants.PATH_DELIMITER + VIEW;

	@GetMapping
	public String get(HttpSession httpSession) {

		log.info("GET {}", PATH);

		httpSession.invalidate();

		return VIEW;
	}
}
