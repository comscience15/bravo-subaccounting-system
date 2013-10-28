package com.bravo.bravoclient.util;

import java.security.PublicKey;

/**
 * This class is used for generating encrypted info by using RSA algorithm
 * @author Daniel danniel1205@gmail.com
 *
 */
public class Encryption {
	private static final String ALGORITHM = "RSA";
	private static PublicKey publicKey;
	
	private static String data;
	private static String encryptedData;
	
	public Encryption(String data) {
		this.data = data;
	}
	
	
	public String generateEncryptedData() {
		return null;
	}
	
	/**
	 * This method is going to call server side API to get public key for RSA encryption
	 */
	private void getPublicKey() {
		
	}
	
	/**
	 * This method is used to encrypt data by using public key from server side
	 * @param text
	 * @param key
	 * @return encrypted data
	 */
	private String encrypt() {
		return null;
	}
	

}
