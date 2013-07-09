package com.bravo.clearTran;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bravo.clearTran.message.AddPaymentProfile;
import com.bravo.clearTran.message.AuthenticateUser;
import com.bravo.clearTran.message.GetPaymentProfile;
import com.bravo.clearTran.message.GetPaymentProfiles;
import com.bravo.clearTran.message.GetToken;
import com.bravo.clearTran.response.AddPaymentProfileResponse;
import com.bravo.clearTran.response.AuthenticateUserResponse;
import com.bravo.clearTran.response.GetPaymentProfileResponse;
import com.bravo.clearTran.response.GetPaymentProfilesResponse;
import com.bravo.clearTran.response.GetTokenResponse;
import com.bravo.webapp.bean.Customer;
import com.bravo.webapp.bean.PaymentProfile;
import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.util.Global;

@Controller
public class ClearTranAPIController {
	
	@Autowired
	private ClearTranAPI api;
	
	public boolean addCheckTransaction(PaymentProfile paymentProfile){
		return true;
	}
	
	public boolean addCreditCardTransaction(PaymentProfile paymentProfile){
		return true;
	}

	public String getToken(){
		GetToken message = new GetToken();
		message.setCtUserName(Global.CTUSERNAME);
		message.setCtPassword(Global.CTPASSWORD);
		message.setAuthorizationID(Global.AUTHORIZAION_ID);
		GetTokenResponse response = api.getToken(message);
		if(response.getStatusInfo().isStatus()){
			return response.getTokenInfo().getToken();
		}
		else {
			throw new UnknownResourceException("ERROR " + response.getErrorInfo().getNumber() + ": " + response.getErrorInfo().getText());
		}
	}
	
	public Customer authenticateUser(String email, String password, String deviceID, String deviceType){
		AuthenticateUser message = new AuthenticateUser();
		message.setCtUsername(Global.CTUSERNAME);
		message.setCtPassword(Global.CTPASSWORD);
		message.setUsername(email);
		message.setPassword(password);
		message.setDeviceID(deviceID);
		message.setDeviceType(deviceType);
		
		AuthenticateUserResponse response = api.authenticateUser(message);
		if(response.getStatusInfo().isStatus()){
			return response.getCustomer();
		}
		else {
			throw new UnknownResourceException("ClearTranAPI ERROR " + response.getErrorInfo().getNumber() + ": " + response.getErrorInfo().getText());
		}
	}

	public Customer addCustomer(Customer customer){
		return api.addCustomer(customer);
	}
	
	public PaymentProfile addPaymentProfile(PaymentProfile profile){
		
		AddPaymentProfile message = new AddPaymentProfile();
		message.setCtUserName(Global.CTUSERNAME);
		message.setCtPassword(Global.CTPASSWORD);
		message.setCustomerID(profile.getCustomerID());
		message.setName(profile.getName());
		message.setPaymentType(profile.getPaymentType());
		message.setAccountNumber(profile.getAccountNumber());
		message.setABA(profile.getABA());
		message.setName1(profile.getName1());
		message.setName2(profile.getName2());
		message.setFirstName(profile.getFirstName());
		message.setMiddleInitial(profile.getMiddleInitial());
		message.setLastName(profile.getLastName());
		message.setCheckProfileType(profile.getCheckProfileType());
		message.setCreditCardProcessingType(profile.getCreditCardProcessingType());
		message.setStreet(profile.getStreet());
		message.setCity(profile.getCity());
		message.setState(profile.getState());
		message.setZip(profile.getZip());
		message.setExpirationDate(profile.getExpirationDate());
		message.setAuthorizationID(Global.AUTHORIZAION_ID);
		message.setToken(this.getToken());
		
		AddPaymentProfileResponse response = api.addPaymentProfile(message);
		if(response.getStatusInfo().isStatus()) {			
			return profile;
		}
		else {
			throw new UnknownResourceException("ClearTranAPI ERROR " + response.getErrorInfo().getNumber() + ": " + response.getErrorInfo().getText());
		}
	}
	
	public List<PaymentProfile> getPaymentProfiles(String customerID)
	{
		GetPaymentProfiles message = new GetPaymentProfiles();
		message.setCtUsername(Global.CTUSERNAME);
		message.setCtPassword(Global.CTPASSWORD);
		message.setCustomerID(customerID);
		message.setAuthorizationID(Global.AUTHORIZAION_ID);
		message.setToken(this.getToken());
		GetPaymentProfilesResponse response = api.getPaymentProfiles(message);
		if(response.getStatusInfo().isStatus()) {
			return response.getPaymentProfiles();
		}
		else {
			throw new UnknownResourceException("ClearTranAPI ERROR " + response.getErrorInfo().getNumber() + ": " + response.getErrorInfo().getText());
		}
	}
	
	public PaymentProfile getPaymentProfile(int paymentProfileID){
		GetPaymentProfile message = new GetPaymentProfile();
		message.setCtUsername(Global.CTUSERNAME);
		message.setCtPassword(Global.CTPASSWORD);
		message.setPaymentProfileID(Integer.toString(paymentProfileID));
		message.setAuthorizationID(Global.AUTHORIZAION_ID);
		message.setToken(this.getToken());
		
		GetPaymentProfileResponse response = api.getPaymentProfile(message);
		if(response.getStatusInfo().isStatus()) {
			return response.getPaymentProfile();
		}
		else {
			throw new UnknownResourceException("ClearTranAPI ERROR " + response.getErrorInfo().getNumber() + ": " + response.getErrorInfo().getText());
		}
	}
}
