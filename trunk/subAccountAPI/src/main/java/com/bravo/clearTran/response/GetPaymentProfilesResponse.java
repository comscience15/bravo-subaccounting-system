package com.bravo.clearTran.response;

import java.util.List;

import com.bravo.webapp.bean.PaymentProfile;

public class GetPaymentProfilesResponse {
	private StatusInformation statusInfo;
	private ErrorInformation	errorInfo;
	private List<PaymentProfile> paymentProfiles;
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
	public List<PaymentProfile> getPaymentProfiles() {
		return paymentProfiles;
	}
	public void setPaymentProfiles(List<PaymentProfile> paymentProfiles) {
		this.paymentProfiles = paymentProfiles;
	}
	
	
}
