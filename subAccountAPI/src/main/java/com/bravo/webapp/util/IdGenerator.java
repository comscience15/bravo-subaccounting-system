package com.bravo.webapp.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;

import com.bravo.webapp.bean.Card;

public class IdGenerator {
	private static IdGenerator generator = null;
	private int cardLen = 8;
	private int secLen = 8;
	
	private IdGenerator(){
	}
	
	public static IdGenerator getInstance(){
		if (generator == null){
			generator = new IdGenerator();
		}
		
		return generator;
	}
	
	public Card cardGenerator(String merchantAccNo, String customerID){
		Card card = new Card();
		card.setBalance(BigDecimal.ZERO);
		card.setCustomerID(customerID);
		card.setEnabled(true);
		card.setLastUse(new Timestamp(System.currentTimeMillis()));
		card.setLoyaltyPoint(BigDecimal.ZERO);
		card.setMerchantAccNo(merchantAccNo);
		card.setPrimaryCard(false);
		
		String data = genRandNum();
		card.setSecurityCode(data.substring(0, secLen));
		card.setCardID(data.substring(data.length() - Math.min(cardLen, data.length())));
		
		return card;
	}
	
	protected String genRandNum(){
		String data = new BigInteger(128, new SecureRandom()).toString(32);
		return data;
//		MessageDigest md;
//		try {
//			md = MessageDigest.getInstance("MD5");
//			md.update(data.getBytes());
//			return new String(md.digest());
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return null;
	}
}
