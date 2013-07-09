package com.bravo.webapp.security.rememberme;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;

import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.security.bean.CustomWebAuthenticationDetails;
import com.bravo.webapp.security.rememberme.dao.CustomPersistentTokenRepository;

public class CustomPersistentRememberMeServieces extends
		PersistentTokenBasedRememberMeServices {
	private CustomPersistentTokenRepository tokenRepository;
	private String username;
	private String domain;
	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	public CustomPersistentRememberMeServieces(String key,
			UserDetailsService userDetailsService,
			CustomPersistentTokenRepository tokenRepository) {
		super(key, userDetailsService, tokenRepository);
		this.tokenRepository = tokenRepository;
	}

	@Override
	protected void onLoginSuccess(HttpServletRequest request,
			HttpServletResponse response,
			Authentication successfulAuthentication) {
		CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) successfulAuthentication
				.getDetails();
		String username = successfulAuthentication.getName();
		domain = details.getDomain();
		String roleType = details.getRoleType();
		username = roleType + ":" + username;

		System.out
				.println("Creating new persistent login for user " + username);
		logger.debug("Creating new persistent login for user " + username);

		PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(
				username, generateSeriesData(), generateTokenData(), new Date());
		try {
			tokenRepository.createNewToken(persistentToken);
			addCookie(persistentToken, request, response);
		} catch (DataAccessException e) {
			logger.error("Failed to save persistent token ", e);
		}
	}

	private void addCookie(PersistentRememberMeToken token,
			HttpServletRequest request, HttpServletResponse response) {
		setCookie(new String[] { token.getSeries(), token.getTokenValue() },
				getTokenValiditySeconds(), request, response);
	}

	@Override
	protected Authentication createSuccessfulAuthentication(
			HttpServletRequest request, UserDetails user) {
		System.out.println("Rememberme username: " + username);
		String[] userParams = username.split(":");
		RememberMeAuthenticationToken result;
		CustomJdbcDaoServiceList customServiceList = (CustomJdbcDaoServiceList) getUserDetailsService();
		if (customServiceList.isCustomer(userParams[0])) {
			CustomerRememberMeAuthenticationToken auth = new CustomerRememberMeAuthenticationToken(
					getKey(), user, authoritiesMapper.mapAuthorities(user
							.getAuthorities()));
			auth.setCustomerID(customServiceList.getCustomerDAO());
			result = auth;
			System.out.println("Customer: ");
		} else {
			RememberMeAuthenticationToken auth = new RememberMeAuthenticationToken(
					getKey(), user, authoritiesMapper.mapAuthorities(user
							.getAuthorities()));
			result = auth;

			System.out.println("Others: ");
		}

		CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) getAuthenticationDetailsSource()
				.buildDetails(request);
		details.setParams(userParams);
		result.setDetails(details);

		System.out.println(result.toString());
		System.out.println((RememberMeAuthenticationToken.class
				.isAssignableFrom(result.getClass())));

		return result;
	}

	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens,
			HttpServletRequest request, HttpServletResponse response) {
		if (cookieTokens.length != 2) {
			throw new InvalidCookieException("Cookie token did not contain "
					+ 2 + " tokens, but contained '"
					+ Arrays.asList(cookieTokens) + "'");
		}

		final String presentedSeries = cookieTokens[0];
		final String presentedToken = cookieTokens[1];

		PersistentRememberMeToken token = tokenRepository
				.getTokenForSeries(presentedSeries);

		if (token == null) {
			// No series match, so we can't authenticate using this cookie
			System.out.println("No persistent token found for series id: "
					+ presentedSeries);
			throw new RememberMeAuthenticationException(
					"No persistent token found for series id: "
							+ presentedSeries);
		}

		// We have a match for this user/series combination
		if (!presentedToken.equals(token.getTokenValue())) {
			// Token doesn't match series value. Delete all logins for this user
			// and throw an exception to warn them.
			tokenRepository.removeUserTokens(token.getUsername());
			System.out
					.println("Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.");
			throw new CookieTheftException(
					messages.getMessage(
							"PersistentTokenBasedRememberMeServices.cookieStolen",
							"Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack."));
		}

		if (token.getDate().getTime() + getTokenValiditySeconds() * 1000L < System
				.currentTimeMillis()) {
			System.out.println("Remember-me login has expired");
			tokenRepository.removeToken(token.getSeries());
			throw new RememberMeAuthenticationException(
					"Remember-me login has expired");
		}

		// Token also matches, so login is valid. Update the token value,
		// keeping the *same* series number.
		if (logger.isDebugEnabled()) {
			logger.debug("Refreshing persistent login token for user '"
					+ token.getUsername() + "', series '" + token.getSeries()
					+ "'");
		}
		System.out
				.println("Refreshing persistent login token for user '"
						+ token.getUsername() + "', series '"
						+ token.getSeries() + "'");

		String[] domainParams = token.getTokenValue().split("/", 2);
		domain = domainParams[0];
		PersistentRememberMeToken newToken = new PersistentRememberMeToken(
				token.getUsername(), token.getSeries(), generateTokenData(),
				new Date());

		try {
			tokenRepository.updateToken(newToken.getSeries(),
					newToken.getTokenValue(), newToken.getDate());
			addCookie(newToken, request, response);
		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error("Failed to update token: ", e);
			throw new RememberMeAuthenticationException(
					"Autologin failed due to data access problem");
		}

		username = token.getUsername() + ":" + domain;

		return getUserDetailsService().loadUserByUsername(username);
	}

	public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
		this.authoritiesMapper = authoritiesMapper;
	}

	@Override
	protected String generateTokenData() {
		return domain + "/" + super.generateTokenData();
	}

	@Override
	public void setTokenRepository(PersistentTokenRepository tokenRepository) {
		throw new UnknownResourceException("Method not support: "
				+ this.getClass().toString());
	}

	@Override
	public void logout(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) {
		if (authentication != null) {
			String rememberMeCookie = extractRememberMeCookie(request);
			String[] cookieTokens = decodeCookie(rememberMeCookie);
			tokenRepository.removeToken(cookieTokens[0]);
			System.out.println("Clear persistent token for user: "
					+ authentication.getName());
		}

		cancelCookie(request, response);
	}

}
