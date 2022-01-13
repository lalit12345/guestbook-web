package com.guestbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guestbook.model.Constants;
import com.guestbook.model.GuestEntryDto;
import com.guestbook.service.SecurityService;
import com.guestbook.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(UserHomeController.PATH)
public class UserHomeController {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserService userService;

	public static final String VIEW = "user-home";

	public static final String PATH = Constants.PATH_DELIMITER + VIEW;

	@GetMapping
	public String get(Model model, HttpSession httpSession, Authentication authentication) {

		log.info("GET {}", PATH);

		if (!securityService.isAuthenticated()) {
			return Constants.REDIRECT + LoginController.PATH;
		}

		model.addAttribute("guestEntryDto", GuestEntryDto.builder().build());

		return VIEW;
	}

	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public String post(Model model, GuestEntryDto guestEntryDto, HttpSession httpSession,
			Authentication authentication) {

		log.info("POST {}", PATH);

		String emailId = authentication.getName();

		if (!StringUtils.hasLength(guestEntryDto.getEntryText())
				&& !StringUtils.hasLength(guestEntryDto.getMultipartFile().getOriginalFilename())) {
			model.addAttribute("invalidEntry", "Add atleast one entry");
		} else {

			userService.addEntry(emailId, guestEntryDto.getEntryText(),
					guestEntryDto.getMultipartFile().getOriginalFilename());

			model.addAttribute("validEntry", "Entry is made successfully");
		}

		return VIEW;
	}
}
