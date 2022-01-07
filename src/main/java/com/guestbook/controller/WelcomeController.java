package com.guestbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(WelcomeController.PATH)
public class WelcomeController {

	public static final String VIEW = "welcome";

	public static final String PATH = "/" + VIEW;

	@GetMapping
	public String get() {

		log.info("GET {}", PATH);

		return VIEW;
	}
}
