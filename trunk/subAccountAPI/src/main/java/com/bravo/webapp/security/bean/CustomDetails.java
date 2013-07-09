package com.bravo.webapp.security.bean;

public interface CustomDetails {
	
	public String getRoleType();
	public String getDomain();
	public String getParameter(String key);
	public void setParameter(String key, String value);
	public String getReqParams();

}
