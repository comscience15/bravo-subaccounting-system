package com.bravo.webapp.transaction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bravo.webapp.bean.Customer;
import com.bravo.webapp.dao.CustomerDAO;
import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.security.CustomAuthenticationDetailsSource;
import com.bravo.webapp.security.CustomAuthenticationProvider;
import com.bravo.webapp.security.LoginInformation;
import com.bravo.webapp.security.bean.CustomWebAuthenticationDetails;
import com.bravo.webapp.security.rememberme.CustomPersistentRememberMeServieces;

public class LoginService {

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UsernamePasswordAuthenticationFilter authentiationFilter;

	@Autowired
	private CustomAuthenticationProvider customerAuthenticationProvider;

	@Autowired
	private CustomAuthenticationDetailsSource customAuthenticationDetailsSource;
	
	@Autowired
	private CustomPersistentRememberMeServieces rememberMeServices;

	public boolean addCustomer(Customer customer) {
		return customerDAO.addCustomer(customer);
	}

	public boolean changePassword(HttpServletRequest request,
			HttpServletResponse response, String oldPassword, String newPassword) {
		// Get username
		String username = LoginInformation.getUsername();

		// Create authentication token
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
				username, oldPassword);
		auth.setDetails(LoginInformation.getDetails());
		try {
			// Authenticate the old password
			authenticationManager.authenticate(auth);
		} catch (AuthenticationException ae) {
			ae.printStackTrace();
			throw new UnknownResourceException(
					"Authentication fail: wrong old password.");
		}

		// Change the old password to the new one
		if (customerDAO.changePassword(username, newPassword)) {
			// Update the authentication token
			auth = new UsernamePasswordAuthenticationToken(username,
					newPassword);
			auth.setDetails(LoginInformation.getDetails());
			Authentication result = authenticationManager.authenticate(auth);
			rememberMeServices.loginSuccess(request, response, result);
			LoginInformation.setAuthentication(result);

			return true;
		}
		
		return false;
	}
	
	public void checkCustomerRole(HttpServletRequest request){
		CustomWebAuthenticationDetails customerDetails = customAuthenticationDetailsSource
				.buildDetails(request);
		if (!customerDetails.getRoleType().equalsIgnoreCase(
				customerAuthenticationProvider.getRoleType())) {
			throw new UnknownResourceException("Wrong Role Type.");
		}
	}
	
	public final String obtainUsername(HttpServletRequest request){
		return request.getParameter(authentiationFilter
				.getUsernameParameter());
	}
	
	public final String obtainPassword(HttpServletRequest request){
		return request.getParameter(authentiationFilter
				.getPasswordParameter());
	}
	
	public final void authenticate(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// Auto-login after successful sign up.
			Authentication auth = authentiationFilter.attemptAuthentication(
					request, response);
			rememberMeServices.loginSuccess(request, response, auth);
			LoginInformation.setAuthentication(auth);
			// save the updated context to the session
			// repository.saveContext(SecurityContextHolder.getContext(),
			// request,
			// response);
			// return new LoginStatusBean(true, auth.isAuthenticated(),
			// auth.getName(), null);
		} catch (AuthenticationException ae) {
			ae.printStackTrace();
			throw new UnknownResourceException("Authentication failed.");
		}

	}
}
