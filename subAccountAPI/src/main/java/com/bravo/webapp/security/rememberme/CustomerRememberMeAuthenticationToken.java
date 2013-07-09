package com.bravo.webapp.security.rememberme;

import java.util.Collection;

import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.bravo.webapp.dao.CustomerDAO;
import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.security.CustomerToken;

public class CustomerRememberMeAuthenticationToken extends
		RememberMeAuthenticationToken implements CustomerToken {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8493837739732246079L;
	private String customerID = null;

	public CustomerRememberMeAuthenticationToken(String key, Object principal,
			Collection<? extends GrantedAuthority> authorities) {
		super(key, principal, authorities);
	}

	@Override
	public String getCustomerID() {
		return customerID;
	}

	@Override
	public void setCustomerID(CustomerDAO customerDAO) {
		if (customerID == null) {
			System.out.println("username: "+getName());
			customerID = customerDAO.getCustomerID(getName());
			System.out.println("customerID: "+customerID);
		} else {
			throw new UnknownResourceException(
					"customID has been set. @CustomerRememberMeAuthenticationToken");
		}

	}

}
