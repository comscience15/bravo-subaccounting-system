package com.bravo.webapp.bean;

import java.sql.Timestamp;

// Mapping to the decrypted information which is used by
// com.bravo.webapp.util.Encryption
public class DecryptedInfo {
	private String cardID;
	private Timestamp customerTimestamp;
	
	public DecryptedInfo(String cardID, Timestamp customerTimestamp){
		this.cardID = cardID;
		this.customerTimestamp = customerTimestamp;
	}
	
	public DecryptedInfo(String cardID, long customerTimestamp){
		this(cardID, new Timestamp(customerTimestamp));
	}
	
	public DecryptedInfo(String cardID, String customerTimestamp){
		this(cardID, Long.parseLong(customerTimestamp));
	}
	
	public String getCardID() {
		return cardID;
	}
	public Timestamp getCustomerTimestamp() {
		return customerTimestamp;
	}

}
