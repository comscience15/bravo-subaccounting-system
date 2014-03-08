package com.bravo.webapp.util;

import java.io.*;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.springframework.core.io.FileSystemResource;

import com.bravo.webapp.bean.DecryptedInfo;
import com.bravo.webapp.exception.UnknownResourceException;

public class Encryption {
	private static int encryptBitLen = 512;
    public static final String algorithm = "RSA";
    public static final long sevenDays = 7 * 24 * 60 * 60 * 1000;
    private final Logger logger = Logger.getLogger(Encryption.this.getClass().getName());

	public Encryption() {
        logger.log(Level.INFO, "The empty Encryption C is called");
		this.encryptBitLen = 512;
	}

	public Encryption(int encryptBitLen) {
        logger.log(Level.INFO, "The one arg Encryption C is called");
        this.encryptBitLen = encryptBitLen;
	}

	public DecryptedInfo infoDecryption(HttpServletRequest request,
			String path, String info) {
		path = request.getSession().getServletContext().getRealPath(path);
		return infoDecryption(path, info);
	}

	public DecryptedInfo infoDecryption(String path, String info) {

		Security.addProvider(new BouncyCastleProvider());

		String cardNumber = null;
		String customerTimestamp = null;
		KeyPair keys = null;

		try {
			keys = loadKeyPair(path, algorithm);

			String decryptedInfo = JCryptionUtil.decrypt(info, keys);

			JsonFactory factory = new JsonFactory();

			JsonParser jp = factory.createJsonParser(decryptedInfo);

			while (jp.nextToken() != JsonToken.END_OBJECT) {

				String fieldname = jp.getCurrentName();
				if ("cardID".equals(fieldname)) {

					jp.nextToken();
					cardNumber = jp.getText();

				}

				if ("customerTimestamp".equals(fieldname)) {

					jp.nextToken();
					customerTimestamp = jp.getText();

				}
			}

			DecryptedInfo result = new DecryptedInfo(cardNumber,
					customerTimestamp);
			logger.info("Card ID is: " + result.getCardID());
			logger.info("Customer timestamp is: " + result.getCustomerTimestamp());

			return result;

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UnknownResourceException("Decryption failed");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new UnknownResourceException("Decryption failed");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UnknownResourceException("Decryption failed");
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UnknownResourceException("Decryption failed");
		}

	}

	public String keyGeneration(String path) {

		JCryptionUtil jCryptionUtil = new JCryptionUtil();

		KeyPair keys = null;

        // More than 7 days, we will refresh the key pairs
        if (System.currentTimeMillis() - JCryptionUtil.keyGeneratedTime < sevenDays) {
		    try {
                //Try to load exist key pairs first
                keys = loadKeyPair(path, algorithm);
		    } catch (IOException e) {
			    // If no key pair exists, go to generate one
                if (e instanceof FileNotFoundException) {
                    logger.info("Key pairs do not exist right now");
                    keys = jCryptionUtil.generateKeypair(encryptBitLen);
                    try {
                        saveKeyPair(path, keys);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (NoSuchAlgorithmException e) {
			    e.printStackTrace();
		    } catch (InvalidKeySpecException e) {
			    e.printStackTrace();
		    }
        } else {
            // generate new keys and save it
            keys = jCryptionUtil.generateKeypair(encryptBitLen);
            try {
                saveKeyPair(path, keys);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

		String e = JCryptionUtil.getPublicKeyExponent(keys);
		String n = JCryptionUtil.getPublicKeyModulus(keys);
		String md = String.valueOf(JCryptionUtil.getMaxDigits(encryptBitLen));

		StringBuffer output = new StringBuffer();

		output.append("{\"e\":\"");
		output.append(e);
		output.append("\",\"n\":\"");
		output.append(n);
		output.append("\",\"maxdigits\":\"");
		output.append(md);
		output.append("\"}");

		String result = output.toString();

		return result;

	}

	public void saveKeyPair(String path, KeyPair keyPair) throws IOException {
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		// Store Public Key.
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
				publicKey.getEncoded());
		logger.info("Store keys to " + path);

		FileOutputStream fos = new FileOutputStream(path + "/public.key");
		fos.write(x509EncodedKeySpec.getEncoded());
		fos.close();

		// Store Private Key.
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				privateKey.getEncoded());
		fos = new FileOutputStream(
				new FileSystemResource(path + "/private.key").getFile());
		fos.write(pkcs8EncodedKeySpec.getEncoded());
		fos.close();
	}

	public KeyPair loadKeyPair(String path, String algorithm)
			throws IOException, NoSuchAlgorithmException,
			InvalidKeySpecException {
        logger.info("Load exist key pairs");
		// Read Public Key.
		File filePublicKey = new File(path + "/public.key");
		FileInputStream fis = new FileInputStream(path + "/public.key");
		byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
		fis.read(encodedPublicKey);
		fis.close();

		// Read Private Key.
		File filePrivateKey = new File(path + "/private.key");
		fis = new FileInputStream(path + "/private.key");
		byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
		fis.read(encodedPrivateKey);
		fis.close();

		// Generate KeyPair.
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
				encodedPrivateKey);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

		return new KeyPair(publicKey, privateKey);
	}

	public int getEncryptBit() {
		return encryptBitLen;
	}

}
