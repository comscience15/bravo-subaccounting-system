package com.bravo.webapp.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.util.Assert;

import com.bravo.webapp.security.bean.CustomWebAuthenticationDetails;

public class CustomAuthenticationDetailsSource
		implements
		AuthenticationDetailsSource<HttpServletRequest, CustomWebAuthenticationDetails> {
	
	public static final String CUSTOM_FORM_DOMAIN_PARAM_KEY = "j_domain";
	public static final String CUSTOM_FORM_ROLE_TYPE_PARAM_KEY = "j_roletype";
	
	protected String domainParameter = CUSTOM_FORM_DOMAIN_PARAM_KEY;
	protected String roletypeParameter = CUSTOM_FORM_ROLE_TYPE_PARAM_KEY;

	@Override
	public CustomWebAuthenticationDetails buildDetails(
			HttpServletRequest request) {
		return new CustomWebAuthenticationDetails(request, domainParameter, roletypeParameter);
	}

	public final String getDomainParameter() {
		return domainParameter;
	}

	public void setDomainParameter(String domainParameter) {
		Assert.hasText(domainParameter, "Domain parameter must not be empty or null");
		this.domainParameter = domainParameter;
	}

	public final String getRoletypeParameter() {
		return roletypeParameter;
	}

	public void setRoletypeParameter(String roletypeParameter) {
		Assert.hasText(roletypeParameter, "RoleType parameter must not be empty or null");
		this.roletypeParameter = roletypeParameter;
	}

}
