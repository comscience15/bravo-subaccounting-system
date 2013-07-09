package com.bravo.webapp.dao;

import java.util.List;

import com.bravo.webapp.bean.Customer;
import com.bravo.webapp.bean.PaymentProfile;

public interface CustomerDAO {

	public String generateCustomerID();

	public String getCustomerID(String username);

	public boolean addCustomer(Customer customer);

	public boolean changePassword(String customerID, String password);

	public boolean updateCustomer(Customer customer);

	public Customer getCustomer(String customerID);

	public boolean addPaymentProfile(PaymentProfile profile);

	public PaymentProfile getPaymentProfile(String name, String customerID);

	public PaymentProfile getPaymentProfile(int paymentProfileID);

	public List<PaymentProfile> getPaymentProfileList(String customerID);

}
