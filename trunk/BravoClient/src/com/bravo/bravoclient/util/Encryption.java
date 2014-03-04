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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.dialogs.BravoAlertDialog;
import com.bravo.https.apicalls.ClientAPICalls;
import com.bravo.https.util.HttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * This class is used for generating encrypted info by using RSA algorithm
 * @author Daniel danniel1205@gmail.com
 *
 */
public class Encryption {
	private static final String ALGORITHM = "RSA";
	private static PublicKey publicKey;
	private static String jsonResponse;
	private Context context;
	private Logger logger = Logger.getLogger(Encryption.class.getName());
	public Encryption(Context context) {
		this.context = context;
		this.jsonResponse = ""; //Reset the jsonResponse for each single time this class is initiated.
	}
	
	/**
	 * Generate the encrypted data, one should get the public key first
	 * @return
	 */
	public String generateEncryptedData(String data) {
		if (publicKey != null) {
			return encrypt(data, publicKey);
		} else {
			logger.warning("public key is null");
			logger.warning("Cannot generate encrypted data");
			return "";
		}
	}
	
	/**
	 * This method is going to call server side API to get public key for RSA encryption
	 */
	public PublicKey getPublicKey(final String IP) {
		KeyFactory factory = null;
		PublicKey pub = null;
		
		// Call the api in order to get the exponent and modulus which are used by RSA for generating the public key
		String status = null;
		String msg = null;
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				doGetPublicKey(IP);
			}
		}).start();
		
		postProgress();

		status = HttpResponseHandler.parseJson(jsonResponse, "status");
		msg = HttpResponseHandler.parseJson(jsonResponse, "message");
		
		if (jsonResponse != null && status.equals("404") == false) {
			String e = HttpResponseHandler.parseJson(msg, "e");
			String n = HttpResponseHandler.parseJson(msg, "n");
			
			logger.info("e: " +e);
			logger.info("m: " + n);
			
			BigInteger exponent = new BigInteger(e, 16);
			BigInteger modulus = new BigInteger(n, 16);
		
			RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
		
			try {
				// Get the instance of key factory
				factory = KeyFactory.getInstance(ALGORITHM);
			} catch (NoSuchAlgorithmException exception) {
				logger.log(Level.WARNING, "Failed to get key factory");
				exception.printStackTrace();
			}
			try {
				// Generate the public key
				pub = factory.generatePublic(spec);
				publicKey = pub;
			} catch (InvalidKeySpecException exception) {
			    logger.log(Level.WARNING, "Invalid public key");
			    exception.printStackTrace();
			}
			return publicKey;
		} else {
		    logger.log(Level.WARNING, "JSON response from API call is null");
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
		    final Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		    // encrypt the plain text using the public key
		    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		    encryptedData = byteArrayToHexString(cipher.doFinal(data.getBytes()));
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		return encryptedData;
	}
	
	private static String byteArrayToHexString(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			result.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return result.toString();
	}
	
	private void doGetPublicKey(String IP) {
		try {
			jsonResponse = ClientAPICalls.getPublicKey(IP, context);
		} catch (KeyManagementException e1) {
			logger.severe(e1.getMessage());
		} catch (UnrecoverableKeyException e1) {
			logger.severe(e1.getMessage());
		} catch (CertificateException e1) {
			logger.severe(e1.getMessage());
		} catch (KeyStoreException e1) {
			logger.severe(e1.getMessage());
		} catch (NoSuchAlgorithmException e1) {
			logger.severe(e1.getMessage());
		} catch (IOException e1) {
			logger.severe(e1.getMessage());
		}	
	}
	
	private void postProgress() {
		int sec = 0;
		int timeout = Integer.valueOf(context.getString(R.string.network_timeout));
		while (jsonResponse == null || jsonResponse.equals("")) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.severe("Timer has been stopped");
			}
			sec ++;
			if (sec >= timeout) {
				new BravoAlertDialog(context).showDialog("Encryption", "Getting public key for encryption time out", "OK");
			}
		}
	}

}
