package com.guestbook.service;

import java.util.List;

import org.springframework.ui.Model;

import com.guestbook.entity.User;
import com.guestbook.model.GuestEntry;
import com.guestbook.model.GuestEntryUpdateDto;
import com.guestbook.model.RegistrationDto;

public interface UserService {

	void registeruser(RegistrationDto registrationDetails, Model model);

	User updateUser(String emailId, String entryText, String entryImagePath);

	List<GuestEntry> getListOfEntries();

	User approve(String emailId);

	User update(GuestEntryUpdateDto guestEntryUpdateDto);

	User delete(String emailId);
}
