package com.guestbook.model;

public enum MessageEnum {

	INVALID_LOGIN_CREDENTIALS_MESSAGE("Invalid emailId or password."),

	LOGOUT_MESSAGE("You have been logged out successfully.");

	private final String attribute;

	private MessageEnum(String name) {
		attribute = name;
	}

	public final String value() {
		return attribute;
	}
}
