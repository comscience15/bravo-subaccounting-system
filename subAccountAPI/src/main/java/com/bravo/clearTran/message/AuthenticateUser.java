package com.bravo.clearTran.message;

public class AuthenticateUser {
	private String CtUsername;
	private String CtPassword;
	private String Username;
	private String Password;
	private String DeviceID;
	private String DeviceType;
	private String AuthorizationID;
	private String Token;
	
	public String getCtUsername() {
		return CtUsername;
	}
	public void setCtUsername(String ctUsername) {
		CtUsername = ctUsername;
	}
	public String getCtPassword() {
		return CtPassword;
	}
	public void setCtPassword(String ctPassword) {
		CtPassword = ctPassword;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getDeviceID() {
		return DeviceID;
	}
	public void setDeviceID(String deviceID) {
		DeviceID = deviceID;
	}
	public String getDeviceType() {
		return DeviceType;
	}
	public void setDeviceType(String deviceType) {
		DeviceType = deviceType;
	}
	public String getAuthorizationID() {
		return AuthorizationID;
	}
	public void setAuthorizationID(String authorizationID) {
		AuthorizationID = authorizationID;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	
	
}
