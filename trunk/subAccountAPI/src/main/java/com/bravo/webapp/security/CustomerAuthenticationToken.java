package com.bravo.webapp.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.bravo.webapp.dao.CustomerDAO;
import com.bravo.webapp.exception.UnknownResourceException;

public class CustomerAuthenticationToken extends
		UsernamePasswordAuthenticationToken implements CustomerToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4967888878708065292L;
	private String customerID = null;

	public CustomerAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

	public CustomerAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}

	@Override
	public String getCustomerID() {
		return customerID;
	}

	@Override
	public void setCustomerID(CustomerDAO customerDAO) {
		if (customerID == null) {
			System.out.println(super.getName());
			customerID = customerDAO.getCustomerID(super.getName());
		} else {
			throw new UnknownResourceException(
					"customID has been set. @CustomerAuthenticationToken");
		}
	}

}
