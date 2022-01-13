package com.guestbook.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guestbook.model.Constants;
import com.guestbook.model.RegistrationDto;
import com.guestbook.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(RegistrationController.PATH)
public class RegistrationController {

	public static final String REGISTRATION_DETAILS = "registrationDto";

	public static final String VIEW = "register";

	public static final String PATH = Constants.PATH_DELIMITER + VIEW;

	@Autowired
	private UserService userService;

	@GetMapping
	public String get(Model model, HttpSession httpSession) {

		log.info("GET {}", PATH);

		model.addAttribute(REGISTRATION_DETAILS, RegistrationDto.builder().build());

		return VIEW;
	}

	@PostMapping
	public String post(@Valid RegistrationDto registrationDto, BindingResult errors, Model model,
			HttpSession httpSession) {

		log.info("POST {}", PATH);

		if (errors.hasErrors()) {
			return VIEW;
		}

		userService.registeruser(registrationDto, model);

		if (StringUtils.hasLength((String) model.getAttribute(Constants.USER_ALREADY_EXISTS))) {

			model.addAttribute(REGISTRATION_DETAILS, RegistrationDto.builder().build());

			return VIEW;
		}

		return Constants.REDIRECT + LoginController.PATH;
	}
}
