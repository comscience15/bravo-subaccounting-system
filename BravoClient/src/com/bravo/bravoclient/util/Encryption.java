package com.bravo.bravoclient.util;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Hashtable;

import javax.crypto.Cipher;

import com.bravo.https.apicalls.CommonAPICalls;

import android.content.Context;

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
	private Context context;
	
	public Encryption(String data, Context context) {
		this.data = data;
		this.context = context;
	}
	
	/**
	 * Generate the encrypted data, one should get the public key first
	 * @return
	 */
	public String generateEncryptedData() {
		if (publicKey != null) {
			return encrypt(data, publicKey);
		} else {
			System.out.println("public key is null");
			return null;
		}
	}
	
	/**
	 * This method is going to call server side API to get public key for RSA encryption
	 */
	public PublicKey getPublicKey(String IP) {
		KeyFactory factory = null;
		PublicKey pub = null;
		
		// Call the api in order to get the exponent and modulus which are used by RSA for generating the public key
		Hashtable<String, BigInteger> publicKeyTable = null;
		try {
			publicKeyTable = CommonAPICalls.getPublicKey(IP, context);
		} catch (KeyManagementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnrecoverableKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CertificateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (KeyStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		if (publicKeyTable != null) {
		
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
			return publicKey;
		} else {
			System.err.println("publicKeyTable from Encryption is null");
			publicKey = null;
			return null;
		}
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
