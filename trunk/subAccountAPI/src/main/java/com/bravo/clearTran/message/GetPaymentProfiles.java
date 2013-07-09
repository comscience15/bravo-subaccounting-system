package com.bravo.clearTran.message;

public class GetPaymentProfiles {
	private String CtUsername;
	private String CtPassword;
	private String CustomerID;
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
	public String getCustomerID() {
		return CustomerID;
	}
	public void setCustomerID(String customerID) {
		CustomerID = customerID;
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
