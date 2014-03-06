package com.bravo.bravoclient.common;

import java.util.logging.Logger;

public class CommonValidationHandler {
	
	private static Logger logger = Logger.getLogger(CommonValidationHandler.class.getName());
	
	public static boolean notNullValidation(String value) {
		if (value != null && !value.isEmpty()) {
			return true;
		} else {
			logger.warning("Input is null");
			return false;
		}
	}

	public static boolean zipCodeValidation(String zipCode) {
		if (zipCode != null && zipCode.matches("[0-9]{5,6}")) {
			return true;
		} else {
			logger.warning("Zip code is not valid");
			return false;
		}
	}
	
	public static boolean passwordValidation(String password) {
		if (password.matches("[a-z0-9A-Z]+") && (password.length() >=6)){
			return true;
		} else {
			logger.warning("Password is not valid");
			return false;
		}
	}
	
	public static boolean usernameValidation(String username) {
		if (username.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}") ) {
			return true;
		} else {
			logger.warning("Username is not valid");
			return false;
		}
	}
	
	public static boolean cardNumberValidation(String cardNumber) {
		if (cardNumber != null && cardNumber.matches("[0-9]{16}")) {
			return true;
		} else {
			logger.warning("Card number is not valid");
			return false;
		}
	}
	
	public static boolean cvnValidation(String cvn) {
		if (cvn != null && cvn.matches("[0-9]{3}")) {
			return true;
		} else {
			logger.warning("CVN is invalid");
			return false;
		}
	}
	
	public static boolean amountValidation(String amount) {
		try {
			if (amount != null && Double.parseDouble(amount) > 0 && amount.matches("[0-9]+\\.{0,1}[0-9]{0,2}")) {
				return true;
			} else {
				return false;
			}
		} catch(NumberFormatException e) {
			logger.warning("Amount is not a number");
			return false;
		}
	}
}
