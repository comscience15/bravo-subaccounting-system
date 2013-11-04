package com.bravo.bravomerchant.util;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Hashtable;

import javax.crypto.Cipher;

/**
 * This class is used for generating encrypted info by using RSA algorithm
 * @author Daniel danniel1205@gmail.com
 *
 */
public class Encryption {
	private static final String ALGORITHM = "RSA";
	private static String data;
	private static PublicKey publicKey;
	private static String IP;
	
	public Encryption(String data) {
		this.data = data;
	}
	
	/**
	 * Generate the encrypted data, one should get the public key first
	 * @return
	 */
	public String generateEncryptedData() {
		return encrypt(data, publicKey);
	}
	
	/**
	 * This method is going to call server side API to get public key for RSA encryption
	 */
	public PublicKey getPublicKey(String IP) {
		KeyFactory factory = null;
		PublicKey pub = null;
		
		// Call the api in order to get the exponent and modulus which are used by RSA for generating the public key
		Hashtable<String, BigInteger> publicKeyTable = APICallsFactory.getPublicKey(IP);	
		BigInteger exponent = publicKeyTable.get("e");
		BigInteger modulus = publicKeyTable.get("n");
		
		RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
		
		
		try {
			// Get the instance of key factory
			factory = KeyFactory.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Failed to get key factory");
			e.printStackTrace();
		}
		try {
			// Generate the public key
			pub = factory.generatePublic(spec);
			publicKey = pub;
		} catch (InvalidKeySpecException e) {
			System.err.println("Invalid public key");
			e.printStackTrace();
		}
		
		return pub;
	}
	
	/**
	 * This method is used to encrypt data by using public key from server side
	 * @param text
	 * @param key
	 * @return encrypted data
	 */
	private String encrypt(String data, PublicKey publicKey) {
		String encryptedData = null;
		try {
			// get an RSA cipher object and print the provider
		    final Cipher cipher = Cipher.getInstance(ALGORITHM);
		    // encrypt the plain text using the public key
		    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		    encryptedData = new String(cipher.doFinal(data.getBytes()));
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		return encryptedData;
	}
	

}
