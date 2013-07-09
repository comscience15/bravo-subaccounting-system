package com.bravo.webapp.bean;

import java.math.BigDecimal;

public class Merchant {

	private String merchantAccNo;
	private String companyName;
	private String companyAddress;
	private BigDecimal balance;
	
	public String getMerchantAccNo() {
		return merchantAccNo;
	}
	public void setMerchantAccNo(String merchantAccNo) {
		this.merchantAccNo = merchantAccNo;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
}
