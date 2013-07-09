package com.bravo.clearTran;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.bravo.clearTran.message.AddCheckTransaction;
import com.bravo.clearTran.message.AddCreditCardTransaction;
import com.bravo.clearTran.message.AddPaymentProfile;
import com.bravo.clearTran.message.AuthenticateUser;
import com.bravo.clearTran.message.GetPaymentProfile;
import com.bravo.clearTran.message.GetPaymentProfiles;
import com.bravo.clearTran.message.GetToken;
import com.bravo.clearTran.response.AddCheckTransactionResponse;
import com.bravo.clearTran.response.AddCreditCardTransactionResponse;
import com.bravo.clearTran.response.AddPaymentProfileResponse;
import com.bravo.clearTran.response.AuthenticateUserResponse;
import com.bravo.clearTran.response.ErrorInformation;
import com.bravo.clearTran.response.GetPaymentProfileResponse;
import com.bravo.clearTran.response.GetPaymentProfilesResponse;
import com.bravo.clearTran.response.GetTokenResponse;
import com.bravo.clearTran.response.ReceiptInformation;
import com.bravo.clearTran.response.StatusInformation;
import com.bravo.clearTran.response.TokenInformation;
import com.bravo.webapp.bean.Customer;
import com.bravo.webapp.bean.PaymentProfile;
import com.bravo.webapp.dao.impl.JdbcCustomerDAO;
public class ClearTranAPI {

	@Autowired
	private JdbcCustomerDAO customerDAO;
	
	public AddCheckTransactionResponse addCheckTransaction(AddCheckTransaction message){
		return null;
	}
	
	public AddCreditCardTransactionResponse addCreditCardTransaction(AddCreditCardTransaction message){
		return null;
	}

	public GetTokenResponse getToken(GetToken message) {
		StatusInformation statusInfo = new StatusInformation();
		statusInfo.setStatus(true);
		ErrorInformation errorInfo = new ErrorInformation();
		ReceiptInformation receiptInfo = new ReceiptInformation(); // TODO: Ask about the returned receipt
		
		TokenInformation tokenInfo = new TokenInformation();
		final int LENGTH = 128;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String lastAccess = dateFormat.format(date);
		String token = this.randomString(LENGTH); 
		tokenInfo.setAuthorizationID(message.getAuthorizationID());
		tokenInfo.setToken(token);
		tokenInfo.setLastAccess(lastAccess);
		
		
		GetTokenResponse response = new GetTokenResponse();
		response.setStatusInfo(statusInfo);
		response.setErrorInfo(errorInfo);
		response.setReceiptInfo(receiptInfo);
		response.setTokenInfo(tokenInfo);
		
		return response;
	}
	
	public AuthenticateUserResponse authenticateUser(AuthenticateUser message) {
		StatusInformation statusInfo = new StatusInformation();
		ErrorInformation errorInfo = new ErrorInformation();
		PaymentProfile paymentProfile = new PaymentProfile();
		// TODO: Ask about the returned payment profile 
		// TODO Authentication on ClearTran part
		Customer customer = customerDAO.getCustomer(customerDAO.getCustomerID(message.getUsername()));
		if(customer == null) {
			statusInfo.setStatus(false);
			String errorMsg = "No Account";
			int errorNumber = 78;
			errorInfo.setText(errorMsg);
			errorInfo.setNumber(errorNumber);
		}
		else {
			statusInfo.setStatus(true);
		}		
		AuthenticateUserResponse response = new AuthenticateUserResponse();
		response.setStatusInfo(statusInfo);
		response.setErrorInfo(errorInfo);		
		response.setPaymentProfile(paymentProfile);
		response.setCustomer(customer);
		return response;
	}

	public Customer addCustomer(Customer customer) {
		return customerDAO.getCustomer(customerDAO.getCustomerID(customer.getEmail()));
		
//		customer.setCustomerID(customerDAO.getCustomerID());
//		if (customerDAO.addCustomer(customer)) {
//			return customer;
//		} else {
//			return null;
//		}
	}

	public AddPaymentProfileResponse addPaymentProfile(AddPaymentProfile message) {
		StatusInformation statusInfo = new StatusInformation();
		ErrorInformation errorInfo = new ErrorInformation();
		ReceiptInformation receiptInfo = new ReceiptInformation(); // TODO: Ask about the returned receipt
				
		int errorNumber;
		String errorText;
		// check if the payment profile name is duplicate
		// if the card is not exist, add the card to the database
		
		if (customerDAO.getPaymentProfile(message.getName(),
				message.getCustomerID()) == null) {
			PaymentProfile profile = new PaymentProfile();
			profile.setCustomerID(message.getCustomerID());
			profile.setName(message.getName());
			profile.setPaymentType(message.getPaymentType());
			profile.setAccountNumber(message.getAccountNumber());
			profile.setABA(message.getABA());
			profile.setName1(message.getName1());
			profile.setName2(message.getName2());
			profile.setFirstName(message.getFirstName());
			profile.setMiddleInitial(message.getMiddleInitial());
			profile.setLastName(message.getLastName());
			profile.setCheckProfileType(message.getCheckProfileType());
			profile.setCreditCardProcessingType(message.getCreditCardProcessingType());
			profile.setStreet(message.getStreet());
			profile.setCity(message.getCity());
			profile.setState(message.getState());
			profile.setZip(message.getZip());
			profile.setExpirationDate(message.getExpirationDate());
			// if the operation is successful, return the card to the caller
			if (customerDAO.addPaymentProfile(profile)) {
				statusInfo.setStatus(true);
			}
			// else, set error information
			else {
				statusInfo.setStatus(false);
				errorNumber = 990;
				errorText = "Invalid Operation.";
				errorInfo.setNumber(errorNumber);
				errorInfo.setText(errorText);
			}						
		}
		// else, set error information
		else {
			statusInfo.setStatus(false);
			errorNumber = 1203;
			errorText = "Duplicate Payment Profile Name.";
			errorInfo.setNumber(errorNumber);
			errorInfo.setText(errorText);
		}
		
		AddPaymentProfileResponse response = new AddPaymentProfileResponse();
		response.setStatusInfo(statusInfo);
		response.setErrorInfo(errorInfo);
		response.setReceiptInfo(receiptInfo);
		
		return response;
	}
	
	public GetPaymentProfilesResponse getPaymentProfiles(GetPaymentProfiles message){
		StatusInformation statusInfo = new StatusInformation();
		ErrorInformation errorInfo = new ErrorInformation();
				
		List<PaymentProfile> paymentProfiles = customerDAO.getPaymentProfileList(message.getCustomerID());
		if (paymentProfiles==null) {
			paymentProfiles = new ArrayList<PaymentProfile>();
		}
//		int errorNumber;
//		String errorText;
//		if(paymentProfiles == null){
//			statusInfo.setStatus(false);
//			errorNumber = 945;
//			errorText = "Internal Error.";
//			errorInfo.setNumber(errorNumber);
//			errorInfo.setText(errorText);
//		}
//		else if(paymentProfiles.isEmpty()){
//			statusInfo.setStatus(false);
//			errorNumber = 200;
//			errorText = "Record not found.";
//			errorInfo.setNumber(errorNumber);
//			errorInfo.setText(errorText);
//		}
//		else{
			statusInfo.setStatus(true);
//		}
		
		GetPaymentProfilesResponse response = new GetPaymentProfilesResponse();
		response.setStatusInfo(statusInfo);
		response.setErrorInfo(errorInfo);
		response.setPaymentProfiles(paymentProfiles);
		
		return response;
	}
	
	public GetPaymentProfileResponse getPaymentProfile(GetPaymentProfile message){
		StatusInformation statusInfo = new StatusInformation();
		ErrorInformation errorInfo = new ErrorInformation();
		
		PaymentProfile profile = customerDAO.getPaymentProfile(Integer.parseInt(message.getPaymentProfileID()));
		
		int errorNumber;
		String errorText;
		if(profile == null){
			statusInfo.setStatus(false);
			errorNumber = 200;
			errorText = "Record not found.";
			errorInfo.setNumber(errorNumber);
			errorInfo.setText(errorText);
		}
		else {
			statusInfo.setStatus(true);
		}
		
		GetPaymentProfileResponse response = new GetPaymentProfileResponse();
		response.setStatusInfo(statusInfo);
		response.setErrorInfo(errorInfo);
		response.setPaymentProfile(profile);
		
		return response;
	}
	
	private String randomString(int length) {
	    Random r = new Random();
	    StringBuilder sb = new StringBuilder();
	    for(int i = 0; i < length; i++) {
	        char c = (char)(r.nextInt((int)(Character.MAX_VALUE)));
	        sb.append(c);
	    }
	    return sb.toString();
	}
}
