package com.guestbook.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "entry")
public class Entry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "email_id", referencedColumnName = "email_id")
	private User user;

	@Column(name = "entry_text")
	private String entryText;

	@Column(name = "entry_image")
	private String entryImage;

	@Column(name = "is_approved", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean isApproved;

	@Column(name = "delete_flag", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean deleteFlag;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "updated_date")
	private LocalDateTime updatedDate;
}
