package com.guestbook.service;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import com.guestbook.entity.Entry;
import com.guestbook.entity.User;
import com.guestbook.model.GuestEntry;
import com.guestbook.model.GuestEntryUpdateDto;
import com.guestbook.model.RegistrationDto;

public interface UserService {

	void registeruser(RegistrationDto registrationDetails, Model model);

	User addEntry(String emailId, String entryText, String entryImagePath);

	Map<String, List<GuestEntry>> getListOfEntries();

	Entry approve(String emailId, String entryId);

	Entry update(GuestEntryUpdateDto guestEntryUpdateDto);

	Entry delete(String emailId, String entryId);
}
