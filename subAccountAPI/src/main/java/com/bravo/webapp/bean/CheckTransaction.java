package com.bravo.webapp.bean;


// Mapping to the table CheckTransaction
public class CheckTransaction extends Transaction {
	
	private String accountNumber;	// 20
	private String ABA;				// 9
	private String checkDate;			// 8
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getABA() {
		return ABA;
	}
	public void setABA(String aBA) {
		ABA = aBA;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}	
}
