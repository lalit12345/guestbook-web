package com.guestbook.model;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestEntryUpdateDto implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Email(message = "{emailId.pattern}")
	@NotBlank(message = "{emailId.not-blank}")
	private String emailId;

	private String entryId;

	private String entryText;
	
	private String entryImage;

}
