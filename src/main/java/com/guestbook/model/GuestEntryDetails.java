package com.guestbook.model;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestEntryDetails implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String entryText;

	private MultipartFile multipartFile;

}
