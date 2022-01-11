package com.guestbook.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "email_id")
	private String emailId;

	private String password;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "entry_text")
	private String entryText;

	@Column(name = "entry_image")
	private String entryImage;

	private String role;

	@Column(name = "is_approved", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean isApproved;

	@Column(name = "delete_flag", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean deleteFlag;

}
