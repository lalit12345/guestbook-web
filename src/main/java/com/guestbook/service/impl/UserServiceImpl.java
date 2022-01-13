package com.guestbook.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import com.guestbook.entity.Entry;
import com.guestbook.entity.User;
import com.guestbook.exception.BusinessException;
import com.guestbook.model.Constants;
import com.guestbook.model.GuestEntry;
import com.guestbook.model.GuestEntryUpdateDto;
import com.guestbook.model.RegistrationDto;
import com.guestbook.repository.EntryRepository;
import com.guestbook.repository.UserRepository;
import com.guestbook.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EntryRepository entryRepository;

	@Override
	public void registeruser(RegistrationDto registrationDetails, Model model) {

		log.info("User registration is in progress.");

		Optional<User> user = userRepository.findByEmailId(registrationDetails.getEmailId());

		if (user.isPresent()) {

			log.error("User already exists with emailId: {}", registrationDetails.getEmailId());

			model.addAttribute(Constants.USER_ALREADY_EXISTS, "User already exists with given emailId");

			return;
		}

		User newUser = createUser(registrationDetails);

		userRepository.save(newUser);

		log.info("User registration is successfull.");
	}

	@Override
	public User addEntry(String emailId, String entryText, String entryImagePath) {

		Optional<User> user = userRepository.findByEmailId(emailId);

		if (!user.isPresent()) {

			log.error("User not found with emailId: {}", emailId);

			throw new BusinessException("User not found");
		}

		User existingUser = user.get();

		LocalDateTime now = LocalDateTime.now();
		Entry newEntry = Entry.builder().entryText(entryText).entryImage(entryImagePath).user(existingUser)
				.createdDate(LocalDateTime.now()).updatedDate(now).build();
		List<Entry> entries = new ArrayList<>();
		entries.add(newEntry);

		existingUser.setEntries(entries);

		userRepository.save(existingUser);

		log.info("Entry added successfully for user: {}", emailId);

		return existingUser;
	}

	@Override
	public Map<String, List<GuestEntry>> getListOfEntries() {

		List<User> users = userRepository.findAllByRole(Constants.USER_ROLE);

		Map<String, List<GuestEntry>> entries = new HashMap<>();

		for (User user : users) {
			String emailId = user.getEmailId();

			List<GuestEntry> guestEntries = user.getEntries().stream().filter(entry -> !entry.isDeleteFlag())
					.map(this::mapUsersToGuestEntry).collect(Collectors.toList());

			if (!CollectionUtils.isEmpty(guestEntries)) {
				entries.put(emailId, guestEntries);
			}
		}

		return entries;
	}

	@Override
	public Entry approve(String emailId, String entryId) {

		Optional<User> user = userRepository.findByEmailId(emailId);

		if (!user.isPresent()) {

			log.error("User not found with emailId: {}", emailId);

			throw new BusinessException("User not found");
		}

		Optional<Entry> entry = entryRepository.findById(Integer.parseInt(entryId));

		if (!entry.isPresent()) {

			log.error("Entry not found");

			throw new BusinessException("Entry not found");
		}

		Entry existingEntry = entry.get();

		existingEntry.setApproved(true);
		existingEntry.setUpdatedDate(LocalDateTime.now());

		entryRepository.save(existingEntry);

		log.info("User entry approved successfully.");

		return existingEntry;
	}

	@Override
	public Entry update(GuestEntryUpdateDto guestEntryUpdateDto) {

		Optional<User> user = userRepository.findByEmailId(guestEntryUpdateDto.getEmailId());

		if (!user.isPresent()) {

			log.error("User not found with emailId: {}", guestEntryUpdateDto.getEmailId());

			throw new BusinessException("User not found");
		}

		Optional<Entry> entry = entryRepository.findById(Integer.parseInt(guestEntryUpdateDto.getEntryId()));

		if (!entry.isPresent()) {

			log.error("Entry not found");

			throw new BusinessException("Entry not found");
		}

		Entry existingEntry = entry.get();

		existingEntry.setEntryText(guestEntryUpdateDto.getEntryText());
		existingEntry.setEntryImage(guestEntryUpdateDto.getEntryImage());
		existingEntry.setUpdatedDate(LocalDateTime.now());

		entryRepository.save(existingEntry);

		log.info("User entry updated successfully.");

		return existingEntry;
	}

	@Override
	public Entry delete(String emailId, String entryId) {

		Optional<User> user = userRepository.findByEmailId(emailId);

		if (!user.isPresent()) {

			log.error("User not found with emailId: {}", emailId);

			throw new BusinessException("User not found");
		}

		Optional<Entry> entry = entryRepository.findById(Integer.parseInt(entryId));

		if (!entry.isPresent()) {

			log.error("Entry not found");

			throw new BusinessException("Entry not found");
		}

		Entry existingEntry = entry.get();

		existingEntry.setDeleteFlag(true);
		existingEntry.setUpdatedDate(LocalDateTime.now());

		entryRepository.save(existingEntry);

		log.info("User entry sucessfully marked as deleted.");

		return existingEntry;
	}

	private User createUser(RegistrationDto registrationDetails) {

		return User.builder().emailId(registrationDetails.getEmailId())
				.password(bcryptPasswordEncoder.encode(registrationDetails.getPassword()))
				.fullName(registrationDetails.getFullName()).mobileNumber(registrationDetails.getMobileNumber())
				.role(Constants.USER_ROLE).build();
	}

	private GuestEntry mapUsersToGuestEntry(Entry entry) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

		String formattedDateTime = entry.getUpdatedDate().format(formatter);

		return GuestEntry.builder().entryId(String.valueOf(entry.getId())).entryText(entry.getEntryText())
				.entryImage(entry.getEntryImage()).lastUpdated(formattedDateTime).isApproved(entry.isApproved())
				.deleteFlag(entry.isDeleteFlag()).build();
	}
}
