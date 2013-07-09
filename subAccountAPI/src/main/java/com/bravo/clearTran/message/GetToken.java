package com.bravo.clearTran.message;

public class GetToken {
	private String CtUserName;
	private String CtPassword;
	private String AuthorizationID;
	
	public String getCtUserName() {
		return CtUserName;
	}
	public void setCtUserName(String ctUserName) {
		CtUserName = ctUserName;
	}
	public String getCtPassword() {
		return CtPassword;
	}
	public void setCtPassword(String ctPassword) {
		CtPassword = ctPassword;
	}
	public String getAuthorizationID() {
		return AuthorizationID;
	}
	public void setAuthorizationID(String authorizationID) {
		AuthorizationID = authorizationID;
	}
	
	
}
