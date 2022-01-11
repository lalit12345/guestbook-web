package com.guestbook.model;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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

	@NotBlank(message = "{fullName.not-blank}")
	private String fullName;

	@Pattern(regexp = "^[0-9]+?|^$", message = "{mobileNumber.pattern}")
	@NotBlank(message = "{mobileNumber.not-blank}")
	private String mobileNumber;

	private String entryText;

}
