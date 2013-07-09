package com.bravo.webapp.dao;

import java.math.BigDecimal;

import com.bravo.webapp.bean.Merchant;
import com.bravo.webapp.bean.MerchantLogin;

public interface MerchantDAO {

	// Create merchant account
	public boolean addMerchant(Merchant merchant);

	// Credit the balance in the merchant account
	public boolean creditMerchantBalance(String accountNo, BigDecimal balance);

	// Debit the balance in the merchant account
	public boolean debitMerchantBalance(String accountNo, BigDecimal balance);

	// Get the merchant information by the merchant account number
	public Merchant getMerchant(String accountNo);

	// Create merchant login account
	public boolean addMerchantLogin(MerchantLogin account);

	// Update the enabled status of the merchant login account
	public boolean updateMerchantLoginEnabled(String accountNo,
			String username, boolean enabled);

	// Update the password of the merchant login account
	public boolean updateMerchantLoginPassword(String accountNo,
			String username, String password);

}
