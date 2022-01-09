package com.guestbook.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.guestbook.entity.User;
import com.guestbook.model.RegistrationDetails;
import com.guestbook.repository.UserRepository;
import com.guestbook.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	public static final String USER_ROLE = "USER";

	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void registeruser(RegistrationDetails registrationDetails) {

		log.info("User registration is in progress.");

		User user = createUser(registrationDetails);

		userRepository.save(user);

		log.info("User registration is successfull.");
	}

	private User createUser(RegistrationDetails registrationDetails) {

		return User.builder()
				.emailId(registrationDetails.getEmailId())
				.password(bcryptPasswordEncoder.encode(registrationDetails.getPassword()))
				.fullName(registrationDetails.getFullName())
				.mobileNumber(registrationDetails.getMobileNumber())
				.role(USER_ROLE).build();
	}
}
