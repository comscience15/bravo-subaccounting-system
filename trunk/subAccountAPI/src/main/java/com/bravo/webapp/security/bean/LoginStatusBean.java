package com.bravo.webapp.security.bean;

public class LoginStatusBean {
	private final boolean success;
	private final boolean loggedIn;
	private final String username;
	private final String errorMessage;

	public LoginStatusBean(boolean success, boolean loggedIn, String username,
			String errorMessage) {
		this.success = success;
		this.loggedIn = loggedIn;
		this.username = username;
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public String getUsername() {
		return username;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
