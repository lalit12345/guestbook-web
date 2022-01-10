package com.guestbook.service;

import com.guestbook.entity.User;
import com.guestbook.model.RegistrationDetails;

public interface UserService {

	void registeruser(RegistrationDetails registrationDetails);

	User updateUser(String emailId, String entryText, String entryImagePath);
}
