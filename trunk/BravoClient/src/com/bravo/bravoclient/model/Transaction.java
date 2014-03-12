package com.bravo.bravoclient.model;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Transaction {
	private String transactionID;
	private String receiptNo;
	private String transactionType;
	private Date transactionDate;
	private String merchantAccNo;
	private BigDecimal totalAmount;
	
	public String getTransactionID() {
		return transactionID;
	}
	
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	
	public String getReceiptNo() {
		if (receiptNo.equals("null")) {
			return "Non-purchase transaction";
		} else {
			return receiptNo;
		}
	}
	
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public String getTransactionDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		//TODO: we should conver the time zone
		//df.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		return df.format(transactionDate);
	}
	
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public String getMerchantAccNo() {
		return merchantAccNo;
	}
	
	public void setMerchantAccNo(String merchantAccNo) {
		this.merchantAccNo = merchantAccNo;
	}
	
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	@Override
	public String toString() {
		String msg = "ReceiptNo.: {0}\nType: {1}\nDate: {2}\nAmount: {3}";
		return MessageFormat.format(msg, new Object[] {this.getReceiptNo(), this.getTransactionType(), this.getTransactionDate(), this.getTotalAmount()});
	}
}
