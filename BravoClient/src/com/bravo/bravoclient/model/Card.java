package com.bravo.bravoclient.model;

public class Card {
	private String cardId;
	private double loyaltyPoint;
	private String merchantAccNo;
	private double balance;
	
	public String getCardId() {
		return cardId;
	}
	
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	
	public double getLoyaltyPoint() {
		return loyaltyPoint;
	}
	
	public void setLoyaltyPoint(int loyaltyPoint) {
		this.loyaltyPoint = loyaltyPoint;
	}
	
	public String getMerchantAccNo() {
		return merchantAccNo;
	}
	
	public void setMerchantAccNo(String merchantAccNo) {
		this.merchantAccNo = merchantAccNo;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
}
