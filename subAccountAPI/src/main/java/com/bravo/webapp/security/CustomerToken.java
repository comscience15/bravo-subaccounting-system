package com.bravo.webapp.security;

import com.bravo.webapp.dao.CustomerDAO;

public interface CustomerToken {

	public String getCustomerID();
	public void setCustomerID(CustomerDAO customerDAO);
}
