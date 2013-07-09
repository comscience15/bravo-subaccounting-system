package com.bravo.clearTran.response;

public class AddPaymentProfileResponse {
	private StatusInformation statusInfo;
	private ErrorInformation	errorInfo;
	private ReceiptInformation receiptInfo;
	
	public StatusInformation getStatusInfo() {
		return statusInfo;
	}
	public void setStatusInfo(StatusInformation statusInfo) {
		this.statusInfo = statusInfo;
	}
	public ErrorInformation getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(ErrorInformation errorInfo) {
		this.errorInfo = errorInfo;
	}
	public ReceiptInformation getReceiptInfo() {
		return receiptInfo;
	}
	public void setReceiptInfo(ReceiptInformation receiptInfo) {
		this.receiptInfo = receiptInfo;
	}
	
	
}
