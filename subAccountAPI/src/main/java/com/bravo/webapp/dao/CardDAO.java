package com.bravo.webapp.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.bravo.webapp.bean.Card;

public interface CardDAO {
	public boolean addCard(Card card);

	public boolean updateCardOwner(String merchantAccNo, String customerID,
			String cardID, String securityCode);

	public boolean updatePrimaryCard(String merchantAccNo, String cardID,
			String customerID);

	public boolean debitBalance(String merchantAccNo, String cardID,
			BigDecimal amount);

	public boolean creditBalance(String merchantAccNo, String cardID,
			BigDecimal amount);

	public boolean creditLoyaltyPoint(String merchantAccount, String cardID,
			BigDecimal loyaltyPoint);

	public boolean debitLoyaltyPoint(String merchantAccount, String cardID,
			BigDecimal loyaltyPoint);

	public Card getCard(String merchantAccNo, String customerID, String cardID);

	public Card getCard(Card card);

	public boolean updateLastUse(String merchantAccNo, String cardID,
			Timestamp mTimestamp);
	
	public boolean deleteCardOwner(String merchantAccNo, String customerID,
			String cardID, String securityCode);
	
	public Card getPrimaryCard(String merchantAccNo, String customerID);
	
	public int getNumCard(String merchantAccNo, String customerID);
	
	public List<Card> getCardList(String merchantAccNo, String customerID);
	
	public List<Card> getCardListByCustID(String customerID);

	public Card getCard(String merchantAccNo, String cardID);

}
