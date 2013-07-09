package com.bravo.webapp.bean;

// Mapping to the table BNYAdmin
public class BNYMAdmin {
	
	private String email;		// 20
	private String password;	// 50
	private boolean enabled = true;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
