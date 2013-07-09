package com.bravo.webapp.bean;


// Mapping to the table CreditTransaction
public class CreditTransaction extends Transaction {

	private String cardNumber;			// 20
	private String expirationDate;		// 4
	private String createDate;			// 8

	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}	
}
