package com.bravo.webapp.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;

// Mapping to the table Card
public class Card {

	private String merchantAccNo;	// 32 UUID
	private String cardID; 			// 32
	private String securityCode;	// 20
	private BigDecimal loyaltyPoint;
	private BigDecimal balance;
	private boolean primaryCard = false;
	private Timestamp lastUse;
	private String customerID;		// 32
	private boolean enabled = true;	// for lost card
	private String companyName;
	
	public String getMerchantAccNo() {
		return merchantAccNo;
	}
	public void setMerchantAccNo(String merchantAccNo) {
		this.merchantAccNo = merchantAccNo;
	}
	public String getCardID() {
		return cardID;
	}
	public void setCardID(String cardID) {
		this.cardID = cardID;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	public BigDecimal getLoyaltyPoint() {
		return loyaltyPoint;
	}
	public void setLoyaltyPoint(BigDecimal loyaltyPoint) {
		this.loyaltyPoint = loyaltyPoint;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public boolean isPrimaryCard() {
		return primaryCard;
	}
	public void setPrimaryCard(boolean primaryCard) {
		this.primaryCard = primaryCard;
	}
	public Timestamp getLastUse() {
		return lastUse;
	}
	public void setLastUse(Timestamp lastUse) {
		this.lastUse = lastUse;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
