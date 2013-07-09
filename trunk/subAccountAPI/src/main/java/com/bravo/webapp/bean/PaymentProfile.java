package com.bravo.webapp.bean;

import com.bravo.webapp.exception.UnknownResourceException;

public class PaymentProfile {
	private int paymentProfileID; // Read only unique ClearTran identifier
	private String name; // Payment Profile Name. Must be unique for each
							// different customer
	private String paymentType; // Type of Payment Profile. Valid values are “A”
								// for ACH, and “C” for credit cards.
	private String accountNumber; // ACH or credit card account number.
	private String ABA; // Required for ACH transactions, ignored for credit
						// card transactions
	private String name1; // Required for ACH transactions, not used for credit
							// card transactions
	private String name2; // Optional for ACH transactions. Used for joint
							// checking accounts, but not required for an ACH
							// transaction. Not used for credit card
							// transactions.
	private String firstName; // Optional for credit card transactions. Not used
								// for ACH transactions.
	private String middleInitial; // Optional for credit card transactions. Not
									// used for ACH transactions.
	private String lastName; // Optional for credit card transactions. Not used
								// for ACH transactions.
	private String checkProfileType; // Required for check transactions only,
										// ignored for credit card transactions.
										// Valid values are “B” = Business, “P”
										// = Personal.
	private String expirationDate; // Required for credit card profiles only,
									// not used for ACH transactions. “MMYY”
									// format.
	private String creditCardProcessingType; // Required for credit card
												// profiles only, not used for
												// ACH transactions. “MMYY”
												// format.
	private String street;
	private String city;
	private String state;
	private String zip;
	private String customerID;

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	private Customer customer;

	public int getPaymentProfileID() {
		return paymentProfileID;
	}

	public void setPaymentProfileID(int paymentProfileID) {
		this.paymentProfileID = paymentProfileID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getABA() {
		return ABA;
	}

	public void setABA(String aBA) {
		ABA = aBA;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCheckProfileType() {
		return checkProfileType;
	}

	public void setCheckProfileType(String checkProfileType) {
		this.checkProfileType = checkProfileType;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getCreditCardProcessingType() {
		return creditCardProcessingType;
	}

	public void setCreditCardProcessingType(String creditCardProcessingType) {
		this.creditCardProcessingType = creditCardProcessingType;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "PaymentProfile [paymentProfileID=" + paymentProfileID
				+ ", name=" + name + ", paymentType=" + paymentType
				+ ", accountNumber=" + accountNumber + ", ABA=" + ABA
				+ ", name1=" + name1 + ", name2=" + name2 + ", firstName="
				+ firstName + ", middleInitial=" + middleInitial
				+ ", lastName=" + lastName + ", checkProfileType="
				+ checkProfileType + ", expirationDate=" + expirationDate
				+ ", creditCardProcessingType=" + creditCardProcessingType
				+ ", street=" + street + ", city=" + city + ", state=" + state
				+ ", zip=" + zip + ", customerID=" + customerID + ", customer="
				+ customer + "]";
	}
	
	public boolean useAccAddr(){
		if (paymentType.equals("A")){
			return false;
		}
		if ((city==null || city.isEmpty()) && (street==null || street.isEmpty()) && (zip == null || zip.isEmpty())) {
			return true;
		}
		if ((city==null || city.isEmpty()) || (street==null || street.isEmpty()) || (zip == null || zip.isEmpty())) {
			throw new UnknownResourceException("Street, City, Zip can not be empty.");
		}
		
		return false;
	}

}
