package com.bravo.webapp.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.bravo.webapp.dao.CustomerDAO;
import com.bravo.webapp.security.bean.CustomWebAuthenticationDetails;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
	private String roleType;
	private boolean customerFlag;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private CustomerDAO customerDAO;

	public CustomAuthenticationProvider(String roleType) {
		this(roleType, null);
		this.customerFlag = false;
	}

	public CustomAuthenticationProvider(String roleType, CustomerDAO customerDAO) {
		this.roleType = roleType;
		this.customerFlag = true;
		this.customerDAO = customerDAO;
	}

	public String getRoleType() {
		return roleType;
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		System.out.println(((CustomWebAuthenticationDetails) authentication
				.getDetails()).getRoleType());
		if (!((CustomWebAuthenticationDetails) authentication.getDetails())
				.getRoleType().equalsIgnoreCase(roleType)) {
			// Role Type does not support
			System.out.println("Role type does not support: supported: "
					+ roleType
					+ " input: "
					+ ((CustomWebAuthenticationDetails) authentication
							.getDetails()).getRoleType());
			return null;
		}
		System.out.println(authentication.toString());

		Assert.isInstanceOf(
				UsernamePasswordAuthenticationToken.class,
				authentication,
				messages.getMessage(
						"AbstractUserDetailsAuthenticationProvider.onlySupports",
						"Only UsernamePasswordAuthenticationToken is supported"));

		// Determine username
		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED"
				: (authentication.getName() + ((CustomWebAuthenticationDetails) authentication
						.getDetails()).getReqParams());
		System.out.println("username: " + username);

		boolean cacheWasUsed = true;
		UserDetails user = this.getUserCache().getUserFromCache(username);

		if (user == null) {
			cacheWasUsed = false;

			try {
				user = retrieveUser(username,
						(UsernamePasswordAuthenticationToken) authentication);
			} catch (UsernameNotFoundException notFound) {
				logger.debug("User '" + username + "' not found");
				System.out.println("User '" + username + "' not found");

				if (hideUserNotFoundExceptions) {
					throw new BadCredentialsException(
							messages.getMessage(
									"AbstractUserDetailsAuthenticationProvider.badCredentials",
									"Bad credentials"));
				} else {
					throw notFound;
				}
			}

			Assert.notNull(user,
					"retrieveUser returned null - a violation of the interface contract");
		}

		try {
			getPreAuthenticationChecks().check(user);
			additionalAuthenticationChecks(user,
					(UsernamePasswordAuthenticationToken) authentication);
		} catch (AuthenticationException exception) {
			if (cacheWasUsed) {
				// There was a problem, so try again after checking
				// we're using latest data (i.e. not from the cache)
				cacheWasUsed = false;
				user = retrieveUser(username,
						(UsernamePasswordAuthenticationToken) authentication);
				getPreAuthenticationChecks().check(user);
				additionalAuthenticationChecks(user,
						(UsernamePasswordAuthenticationToken) authentication);
			} else {
				throw exception;
			}
		}

		getPostAuthenticationChecks().check(user);

		if (!cacheWasUsed) {
			this.getUserCache().putUserInCache(user);
		}

		Object principalToReturn = user;

		if (isForcePrincipalAsString()) {
			principalToReturn = user.getUsername();
		}

		Authentication auth = createSuccessAuthentication(principalToReturn,
				authentication, user);
		System.out.println(auth.toString());

		return auth;
	}

	@Override
	protected void doAfterPropertiesSet() throws Exception {
		super.doAfterPropertiesSet();
		Assert.notNull(this.roleType, "A roleType must be set");
	}

	@Override
	protected Authentication createSuccessAuthentication(Object principal,
			Authentication authentication, UserDetails user) {
		if (customerFlag) {
			CustomerAuthenticationToken result = new CustomerAuthenticationToken(
					principal, authentication.getCredentials(),
					authoritiesMapper.mapAuthorities(user.getAuthorities()));
			result.setCustomerID(customerDAO);
			result.setDetails(authentication.getDetails());
			
			return result;
		}
		return super.createSuccessAuthentication(principal, authentication,
				user);
	}

	public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
		this.authoritiesMapper = authoritiesMapper;
	}
	
	public UserDetailsService getUserDetailsService(){
		return super.getUserDetailsService();
	}

	public boolean isCustomerFlag() {
		return customerFlag;
	}

}
