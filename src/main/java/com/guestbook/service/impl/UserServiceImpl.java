package com.guestbook.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.guestbook.entity.User;
import com.guestbook.exception.BusinessException;
import com.guestbook.model.Constants;
import com.guestbook.model.RegistrationDetails;
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
	public void registeruser(RegistrationDetails registrationDetails) {

		log.info("User registration is in progress.");

		Optional<User> user = userRepository.findByEmailId(registrationDetails.getEmailId());

		if (user.isPresent()) {

			log.error("User already exists with emailId: {}", registrationDetails.getEmailId());

			throw new BusinessException("User already exists");
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

	private User createUser(RegistrationDetails registrationDetails) {

		return User.builder().emailId(registrationDetails.getEmailId())
				.password(bcryptPasswordEncoder.encode(registrationDetails.getPassword()))
				.fullName(registrationDetails.getFullName()).mobileNumber(registrationDetails.getMobileNumber())
				.role(Constants.USER_ROLE).build();
	}
}
