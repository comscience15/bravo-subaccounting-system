package com.bravo.webapp.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.security.bean.CustomWebAuthenticationDetails;


// Obtain login information from the cache
public class LoginInformation {

	public static String getUsername() {
		return getAuthentication().getName();
	}

	public static String getMerchantAccNo() {
		return ((CustomWebAuthenticationDetails) getDetails()).getDomain();
	}

	public static String getCustomerID() {
		Authentication auth = getAuthentication();
		if (auth instanceof CustomerToken) {
			String customerID = ((CustomerToken) auth).getCustomerID();
			if (customerID.isEmpty()) {
				throw new UnknownResourceException("Empty customerID.");
			}
			
			return customerID;
		}

		return null;
	}

	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static Object getDetails() {
		return getAuthentication().getDetails();
	}

	public static void setAuthentication(
			Authentication auth) {
		SecurityContextHolder.getContext().setAuthentication(auth);		
	}

}
