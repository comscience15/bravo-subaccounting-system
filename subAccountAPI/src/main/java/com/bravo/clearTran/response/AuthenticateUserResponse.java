package com.bravo.clearTran.response;

import com.bravo.webapp.bean.Customer;
import com.bravo.webapp.bean.PaymentProfile;

public class AuthenticateUserResponse {
	private StatusInformation statusInfo;
	private ErrorInformation errorInfo;
	private PaymentProfile paymentProfile;
	private Customer customer;
	
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
	public PaymentProfile getPaymentProfile() {
		return paymentProfile;
	}
	public void setPaymentProfile(PaymentProfile paymentProfile) {
		this.paymentProfile = paymentProfile;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	
}
