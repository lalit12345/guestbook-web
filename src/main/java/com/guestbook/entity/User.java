package com.guestbook.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "email_id")
	private String emailId;

	private String password;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "mobile_number")
	private String mobileNumber;

//	@Setter(AccessLevel.NONE)
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Entry> entries;

	private String role;

//	public void setEntries(Entry entry) {
//		if (entries == null) {
//			entries = new ArrayList<Entry>();
//		}
//		entries.add(entry);
//		entry.setUser(this);
//	}
}
