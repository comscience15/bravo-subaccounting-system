package com.bravo.webapp.security.bean;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.Assert;

import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.security.bean.CustomDetails;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails
		implements CustomDetails {
	/**
	 * 
	 */
	public static final String USERNAME_SEPARATION_KEY = ":";

	private static final long serialVersionUID = -3417943827694809656L;

	private boolean modified = true;
	private String roleType;
	private String domain;
	private Map<String, String> parameterMap = null;
	protected static String usernameSeparationKey = USERNAME_SEPARATION_KEY;
	protected String domainParameter;
	protected String roletypeParameter;

	public CustomWebAuthenticationDetails(HttpServletRequest request, String domainParameter, String roletypeParameter) {
		super(request);
		this.domainParameter = domainParameter;
		this.roletypeParameter = roletypeParameter;
		doPopulateAdditionalInformation(request);
	}

	protected void doPopulateAdditionalInformation(HttpServletRequest request) {
		roleType = request.getParameter(roletypeParameter);
		domain = request.getParameter(domainParameter);
		
		if (roleType == null || roleType.isEmpty()){
			modified = false;
			return;
		}
		if (domain == null) {
			domain = "";
		}

		roleType = roleType.trim();
		domain = domain.trim();
	}
	
	public void setParams(String[] params){
		if (modified){
			throw new UnknownResourceException("Can");
		}
		modified = true;
		roleType = params[0];
		
		Assert.notNull(roleType, "roletype can't be null.");
		if (params.length >= 3) {
			domain = params[2];
		} else {
			domain = "";
		}
		
	}

	@Override
	public final String getRoleType() {
		return roleType;
	}

	@Override
	public final String getDomain() {
		return domain;
	}

	@Override
	public final String getParameter(String key) {
		return parameterMap.get(key);
	}

	public static String getUsernameSeparationKey() {
		return usernameSeparationKey;
	}

	public static void setUsernameSeparationKey(String usernameSeparationKey) {
		CustomWebAuthenticationDetails.usernameSeparationKey = usernameSeparationKey;
	}

	@Override
	public String getReqParams() {
		if (domain.isEmpty()) {
			return "";
		}
		return usernameSeparationKey + domain;
	}

	public String getDomainParameter() {
		return domainParameter;
	}

	public String getRoletypeParameter() {
		return roletypeParameter;
	}

	@Override
	public void setParameter(String key, String value) {
		if(parameterMap == null) {
			parameterMap = new HashMap<String, String>();
		}
		parameterMap.put(key, value);
		
	}

}
