package com.bravo.clearTran.response;

public class GetTokenResponse {
	private StatusInformation statusInfo;
	private ErrorInformation errorInfo;
	private ReceiptInformation receiptInfo;
	private TokenInformation tokenInfo;
	
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
	public TokenInformation getTokenInfo() {
		return tokenInfo;
	}
	public void setTokenInfo(TokenInformation tokenInfo) {
		this.tokenInfo = tokenInfo;
	}
	
	
}
