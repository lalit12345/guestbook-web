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
public class GuestEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String entryId;

	private String emailId;

	private String entryText;

	private String entryImage;

	private boolean isApproved;

	private boolean deleteFlag;

	private String lastUpdated;
}
