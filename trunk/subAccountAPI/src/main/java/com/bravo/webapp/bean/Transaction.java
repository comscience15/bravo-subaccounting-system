package com.bravo.webapp.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Transaction {

	private long transactionId;
	private BigDecimal totalAmount;
	private Long transactionDate;
	private String note; // 50
	private Integer receiptNo;
	private String location; // 50
	private String transactionType; // 50
	private String merchantAccNo; // 32
	private String cardID; // 32
	private Long customerTimestamp;
	private List<OrderItem> orderItemList;

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Timestamp getTransactionDate() {
		if (transactionDate == null) {
			return null;
		}
		
		return new Timestamp(transactionDate);
	}

	public void setTransactionDate(Timestamp transactionDate) {
		if (transactionDate != null) {
			this.transactionDate = transactionDate.getTime();
		} else {
			this.transactionDate = null;
		}
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(Integer receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

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

	public Timestamp getCustomerTimestamp() {
		if (customerTimestamp == null) {
			return null;
		}
		return new Timestamp(customerTimestamp);
	}

	public void setCustomerTimestamp(Timestamp cTimestamp) {
		if (cTimestamp != null){
		this.customerTimestamp = cTimestamp.getTime();
		} else {
			this.customerTimestamp = null;
		}
	}

	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", totalAmount="
				+ totalAmount + ", transactionDate=" + transactionDate
				+ ", note=" + note + ", receiptNo=" + receiptNo + ", location="
				+ location + ", transactionType=" + transactionType
				+ ", merchantAccNo=" + merchantAccNo + ", cardID=" + cardID
				+ ", customerTimestamp=" + customerTimestamp
				+ ", orderItemList=" + orderItemList + "]";
	}

}
