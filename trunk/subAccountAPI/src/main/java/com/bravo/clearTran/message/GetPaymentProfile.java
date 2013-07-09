package com.bravo.clearTran.message;

public class GetPaymentProfile {
	private String CtUsername;
	private String CtPassword;
	private String PaymentProfileID;
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
	public String getPaymentProfileID() {
		return PaymentProfileID;
	}
	public void setPaymentProfileID(String paymentProfileID) {
		PaymentProfileID = paymentProfileID;
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
