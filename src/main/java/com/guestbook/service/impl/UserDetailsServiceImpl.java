package com.guestbook.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.guestbook.entity.User;
import com.guestbook.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {

		Optional<User> user = userRepository.findByEmailId(emailId);

		if (!user.isPresent()) {

			throw new UsernameNotFoundException("User not found");
		}

		return new CustomUserDetails(user.get());
	}

}
