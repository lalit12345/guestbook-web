package com.guestbook.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credentials implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String emailId;

	private String password;
}
