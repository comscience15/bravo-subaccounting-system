package com.bravo.clearTran.message;

public class AddPaymentProfile {
	private String CtUserName;
	private String CtPassword;
	private String CustomerID;
	private String Name; // Payment Profile Name. Must be unique for each different customer
	private String PaymentType; // Type of Payment Profile. Valid values are �A� for ACH, and �C� for credit cards.
	private String AccountNumber; // ACH or credit card account number.
	private String ABA; // Required for ACH transactions, ignored for credit card transactions
	private String Name1; // Required for ACH transactions, not used for credit card transactions
	private String Name2; // Optional for ACH transactions. Used for joint checking accounts, but not required for an ACH transaction. Not used for credit card transactions.
	private String FirstName; // Optional for credit card transactions. Not used for ACH transactions.
	private String MiddleInitial; // Optional for credit card transactions. Not used for ACH transactions.
	private String LastName; // Optional for credit card transactions. Not used for ACH transactions.
	private String CheckProfileType; // Required for check transactions only, ignored for credit card transactions. Valid values are �B� = Business, �P� = Personal.
	private String ExpirationDate; // Required for credit card profiles only, not used for ACH transactions. �MMYY� format.
	private String CreditCardProcessingType; // Required for credit card profiles only, not used for ACH transactions. �MMYY� format.
	private String Street; 
	private String City;
	private String State;
	private String Zip;
	private String AuthorizationID;
	private String Token;
	public String getCtUserName() {
		return CtUserName;
	}
	public void setCtUserName(String ctUserName) {
		CtUserName = ctUserName;
	}
	public String getCtPassword() {
		return CtPassword;
	}
	public void setCtPassword(String ctPassword) {
		CtPassword = ctPassword;
	}
	public String getCustomerID() {
		return CustomerID;
	}
	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getPaymentType() {
		return PaymentType;
	}
	public void setPaymentType(String paymentType) {
		PaymentType = paymentType;
	}
	public String getAccountNumber() {
		return AccountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		AccountNumber = accountNumber;
	}
	public String getABA() {
		return ABA;
	}
	public void setABA(String aBA) {
		ABA = aBA;
	}
	public String getName1() {
		return Name1;
	}
	public void setName1(String name1) {
		Name1 = name1;
	}
	public String getName2() {
		return Name2;
	}
	public void setName2(String name2) {
		Name2 = name2;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getMiddleInitial() {
		return MiddleInitial;
	}
	public void setMiddleInitial(String middleInitial) {
		MiddleInitial = middleInitial;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getCheckProfileType() {
		return CheckProfileType;
	}
	public void setCheckProfileType(String checkProfileType) {
		CheckProfileType = checkProfileType;
	}
	public String getExpirationDate() {
		return ExpirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		ExpirationDate = expirationDate;
	}
	public String getCreditCardProcessingType() {
		return CreditCardProcessingType;
	}
	public void setCreditCardProcessingType(String creditCardProcessingType) {
		CreditCardProcessingType = creditCardProcessingType;
	}
	public String getStreet() {
		return Street;
	}
	public void setStreet(String street) {
		Street = street;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getZip() {
		return Zip;
	}
	public void setZip(String zip) {
		Zip = zip;
	}
	public String getAuthorizationID() {
		return AuthorizationID;
	}
	public void setAuthorizationID(String authorizationID) {
		AuthorizationID = authorizationID;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	
	

}
