package com.guestbook.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guestbook.model.Constants;
import com.guestbook.model.GuestEntry;
import com.guestbook.model.GuestEntryUpdateDto;
import com.guestbook.service.SecurityService;
import com.guestbook.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping
public class AdminHomeController {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserService userService;

	public static final String VIEW = "admin-home";

	public static final String PATH = Constants.PATH_DELIMITER + VIEW;

	public static final String VIEW_PATH = Constants.PATH_DELIMITER + "view";

	private static final String EMPTY_LIST = "emptyList";

	private static final String ENTRIES = "entries";

	private static final String GUEST_ENTRY_UPDATE_DTO_ATTRIBITE = "guestEntryUpdateDto";

	private static final String ENTRY_UPDATED_SUCCESSFULLY = "Entry updated successfully";

	private static final String ADD_ATLEAST_ONE_ENTRY = "Please input a message or an image";

	@GetMapping(PATH)
	public String get(HttpSession httpSession, Authentication authentication) {

		log.info("GET {}", PATH);

		if (!securityService.isAuthenticated()) {
			return Constants.REDIRECT + LoginController.PATH;
		}

		return VIEW;
	}

	@GetMapping(VIEW_PATH)
	public String getListOfEntries(Model model) {

		log.info("GET {}", VIEW_PATH);

		if (!securityService.isAuthenticated()) {
			return Constants.REDIRECT + LoginController.PATH;
		}

		Map<String, List<GuestEntry>> entries = userService.getListOfEntries();

		if (CollectionUtils.isEmpty(entries)) {
			model.addAttribute(EMPTY_LIST, true);
		} else {
			model.addAttribute(ENTRIES, entries);
		}

		model.addAttribute(GUEST_ENTRY_UPDATE_DTO_ATTRIBITE, GuestEntryUpdateDto.builder().build());

		return VIEW;
	}

	@PostMapping("/approve/{userId}/{entryId}")
	public String approve(@NotBlank @PathVariable(name = "userId") String userId,
			@PathVariable(name = "entryId") String entryId, Model model) {

		log.info("POST {}", "/approve");

		if (!securityService.isAuthenticated()) {
			return Constants.REDIRECT + LoginController.PATH;
		}

		userService.approve(userId, entryId);

		fetchEntries(model);

		return VIEW;
	}

	@PostMapping("/update")
	public String updateEntry(GuestEntryUpdateDto guestEntryUpdateDto, BindingResult errors, Model model) {

		log.info("POST {}", "/update");

		if (!securityService.isAuthenticated()) {
			return Constants.REDIRECT + LoginController.PATH;
		}

		if (!StringUtils.hasLength(guestEntryUpdateDto.getEntryText())
				&& !StringUtils.hasLength(guestEntryUpdateDto.getEntryImage())) {

			model.addAttribute(Constants.INVALID_ENTRY, ADD_ATLEAST_ONE_ENTRY);
			model.addAttribute("entryId", guestEntryUpdateDto.getEntryId());

		} else {
			userService.update(guestEntryUpdateDto);

			model.addAttribute(Constants.VALID_ENTRY, ENTRY_UPDATED_SUCCESSFULLY);
		}

		fetchEntries(model);

		return VIEW;
	}

	@GetMapping("/delete/{userId}/{entryId}")
	public String deleteEntry(@NotBlank @PathVariable(name = "userId") String userId,
			@PathVariable(name = "entryId") String entryId, Model model) {

		log.info("GET {}", "delete");

		if (!securityService.isAuthenticated()) {
			return Constants.REDIRECT + LoginController.PATH;
		}

		userService.delete(userId, entryId);

		fetchEntries(model);

		return VIEW;
	}

	private void fetchEntries(Model model) {

		Map<String, List<GuestEntry>> entries = userService.getListOfEntries();

		model.addAttribute(ENTRIES, entries);
	}
}
