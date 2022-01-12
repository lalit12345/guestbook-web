package com.guestbook.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.guestbook.entity.User;
import com.guestbook.exception.BusinessException;
import com.guestbook.model.Constants;
import com.guestbook.model.GuestEntry;
import com.guestbook.model.GuestEntryUpdateDto;
import com.guestbook.model.RegistrationDto;
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
	public User updateUser(String emailId, String entryText, String entryImagePath) {

		Optional<User> user = userRepository.findByEmailId(emailId);

		if (!user.isPresent()) {

			log.error("User not found with emailId: {}", emailId);

			throw new BusinessException("User not found");
		}

		User existingUser = user.get();

		existingUser.setEntryText(entryText);
		existingUser.setEntryImage(entryImagePath);

		userRepository.save(existingUser);

		log.info("User updated successfully");

		return existingUser;
	}

	@Override
	public List<GuestEntry> getListOfEntries() {

		List<User> users = userRepository.findAllByRoleAndDeleteFlag(Constants.USER_ROLE, false);

		List<GuestEntry> entries = users.stream().map(this::mapUsersToGuestEntry).collect(Collectors.toList());

		return entries;
	}

	@Override
	public User approve(String emailId) {

		User user = userRepository.findByEmailId(emailId).get();

		user.setApproved(true);

		userRepository.save(user);

		log.info("User entry approved successfully.");

		return user;
	}

	@Override
	public User update(GuestEntryUpdateDto guestEntryUpdateDto) {

		User user = userRepository.findByEmailId(guestEntryUpdateDto.getEmailId()).get();

		user.setEmailId(guestEntryUpdateDto.getEmailId());
		user.setFullName(guestEntryUpdateDto.getFullName());
		user.setMobileNumber(guestEntryUpdateDto.getMobileNumber());
		user.setEntryText(guestEntryUpdateDto.getEntryText());

		userRepository.save(user);

		log.info("User entry updated successfully.");

		return user;
	}

	@Override
	public User delete(String emailId) {

		User user = userRepository.findByEmailId(emailId).get();

		user.setDeleteFlag(true);

		userRepository.save(user);

		log.info("User entry sucessfully marked as deleted.");

		return user;
	}

	private User createUser(RegistrationDto registrationDetails) {

		return User.builder().emailId(registrationDetails.getEmailId())
				.password(bcryptPasswordEncoder.encode(registrationDetails.getPassword()))
				.fullName(registrationDetails.getFullName()).mobileNumber(registrationDetails.getMobileNumber())
				.role(Constants.USER_ROLE).build();
	}

	private GuestEntry mapUsersToGuestEntry(User user) {

		return GuestEntry.builder().emailId(user.getEmailId()).fullName(user.getFullName())
				.mobileNumber(user.getMobileNumber()).entryText(user.getEntryText()).entryImage(user.getEntryImage())
				.isApproved(user.isApproved()).deleteFlag(user.isDeleteFlag()).build();
	}
}
