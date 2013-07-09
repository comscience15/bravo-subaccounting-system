package com.bravo.clearTran.response;

public class TokenInformation {
	private String AuthorizationID;
	private String Token;
	private String LastAccess;
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
	public String getLastAccess() {
		return LastAccess;
	}
	public void setLastAccess(String lastAccess) {
		LastAccess = lastAccess;
	}
	
	
}
