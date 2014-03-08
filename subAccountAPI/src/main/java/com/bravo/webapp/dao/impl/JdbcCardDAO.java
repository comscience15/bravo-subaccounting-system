package com.bravo.webapp.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.bravo.webapp.bean.Card;
import com.bravo.webapp.dao.CardDAO;
import com.bravo.webapp.exception.UnknownResourceException;

public class JdbcCardDAO extends JdbcDaoSupport implements CardDAO {

    private static Logger logger = Logger.getLogger(JdbcCardDAO.class.getName());

	@Override
	public boolean addCard(Card card) {
		String sql = "INSERT INTO Card (merchantAccNo, cardID, securityCode, loyaltyPoint, balance, primaryCard, lastUse, customerID, enabled) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			int result = getJdbcTemplate().update(sql, card.getMerchantAccNo(),
					card.getCardID(), card.getSecurityCode(),
					card.getLoyaltyPoint(), card.getBalance(),
					card.isPrimaryCard(), card.getLastUse(),
					card.getCustomerID(), card.isEnabled());
			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateCardOwner(String merchantAccNo, String customerID,
			String cardID, String securityCode) {
		Timestamp lastUse = new Timestamp(System.currentTimeMillis());

		String sql = "UPDATE Card SET customerID=?, lastUse=? "
				+ "WHERE merchantAccNo=? AND cardID=? AND securityCode=? AND customerID IS NULL AND enabled=1";
		try {
			int result = getJdbcTemplate().update(sql, customerID, lastUse,
					merchantAccNo, cardID, securityCode);

			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new UnknownResourceException(
					"Update Card Owner Failed: customerID= " + customerID);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean deleteCardOwner(String merchantAccNo, String customerID,
			String cardID, String securityCode) {
		Timestamp lastUse = new Timestamp(System.currentTimeMillis());

		String sql = "UPDATE Card SET customerID=NULL, lastUse=?, primaryCard=0 "
				+ "WHERE merchantAccNo=? AND customerID=? AND cardID=? AND securityCode=? AND enabled=1";
		try {
			int result = getJdbcTemplate().update(sql, lastUse, merchantAccNo,
					customerID, cardID, securityCode);

			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new UnknownResourceException(
					"Delete Card Owner Failed: customerID= " + customerID);
		} catch (Exception e) {
			e.printStackTrace();
		}

		throw new UnknownResourceException("Can't remove the card");
	}

	@Override
	public boolean updatePrimaryCard(String merchantAccNo, String cardID,
			String customerID) {
		String sql1 = "UPDATE Card SET primaryCard = 0 WHERE merchantAccNo = ? AND customerID = ? AND primaryCard=1";
		String sql2 = "UPDATE Card SET primaryCard = 1 WHERE merchantAccNo = ? AND cardID = ? AND enabled=1";
		try {
			getJdbcTemplate().update(sql1, merchantAccNo, customerID);
			int result = getJdbcTemplate().update(sql2, merchantAccNo, cardID);

			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		throw new UnknownResourceException("Can't update the primary card");
	}

	@Override
	public boolean creditBalance(String merchantAccNo, String cardID,
			BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new UnknownResourceException("Amount can not be negative.");
		}

		String sql = "UPDATE Card SET balance = (balance + ?) "
				+ "WHERE cardID=? AND merchantAccNo=? AND enabled=1";
		try {
			int result = getJdbcTemplate().update(sql, amount, cardID,
					merchantAccNo);

			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean debitBalance(String merchantAccNo, String cardID,
			BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new UnknownResourceException("Amount can not be negative.");
		}

		String sql = "UPDATE Card SET balance = (balance - ?) "
				+ "WHERE cardID=? AND merchantAccNo=? AND enabled=1";
		try {
			int result = getJdbcTemplate().update(sql, amount, cardID,
					merchantAccNo);

			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean creditLoyaltyPoint(String merchantAccount, String cardID,
			BigDecimal loyaltyPoint) {
		if (loyaltyPoint.compareTo(BigDecimal.ZERO) < 0) {
			throw new UnknownResourceException(
					"loyaltyPoint can not be negative.");
		}
		String sql = "UPDATE Card SET loyaltyPoint = (loyaltyPoint + ?) "
				+ "WHERE cardID = ? AND merchantAccNo = ? AND enabled=1";
		try {
			int result = getJdbcTemplate().update(sql, loyaltyPoint, cardID,
					merchantAccount);
			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean debitLoyaltyPoint(String merchantAccount, String cardID,
			BigDecimal loyaltyPoint) {
		if (loyaltyPoint.compareTo(BigDecimal.ZERO) < 0) {
			throw new UnknownResourceException(
					"loyaltyPoint can not be negative.");
		}

		String sql = "UPDATE Card SET loyaltyPoint = (loyaltyPoint - ?) "
				+ "WHERE cardID = ? AND merchantAccNo = ? AND enabled=1";
		try {
			int result = getJdbcTemplate().update(sql, loyaltyPoint, cardID,
					merchantAccount);

			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public Card getCard(String merchantAccNo, String customerID, String cardID) {
		try {
			String sql = "SELECT * FROM Card "
					+ "WHERE merchantAccNo = ? AND customerID=? AND cardID = ?";
			Card card = getJdbcTemplate().queryForObject(sql,
					new BeanPropertyRowMapper<Card>(Card.class), merchantAccNo,
					customerID, cardID);
			
			return card;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Card getCard(String merchantAccNo, String cardID) {
		try {
			String sql = "SELECT * FROM Card "
					+ "WHERE merchantAccNo = ? AND cardID = ?";
			Card card = getJdbcTemplate().queryForObject(sql,
					new BeanPropertyRowMapper<Card>(Card.class), merchantAccNo,
					cardID);
			
			return card;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Card getCard(Card card) {
		return getCard(card.getMerchantAccNo(), card.getCustomerID(),
				card.getCardID());
	}

	@Override
	public Card getPrimaryCard(String merchantAccNo, String customerID) {
		try {
			String sql = "SELECT * FROM Card "
					+ "WHERE merchantAccNo = ? AND customerID = ? AND primaryCard=1 AND enabled=1";
			Card card = getJdbcTemplate().queryForObject(sql,
					new BeanPropertyRowMapper<Card>(Card.class), merchantAccNo,
					customerID);
			
			return card;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public int getNumCard(String merchantAccNo, String customerID) {
		try {
			String sql = "SELECT COUNT(cardID) FROM Card "
					+ "WHERE merchantAccNo = ? AND customerID = ? AND enabled=1";

			int nCard = getJdbcTemplate().queryForObject(sql, Integer.class,
					merchantAccNo, customerID);
			return nCard;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;

	}

	@Override
	public List<Card> getCardList(String merchantAccNo, String customerID) {
		try {
			String sql = "SELECT * FROM Card "
					+ "WHERE merchantAccNo = ? AND customerID = ? AND enabled=1 ORDER BY cardID";

			List<Card> cardList = getJdbcTemplate().query(sql,
					new BeanPropertyRowMapper<Card>(Card.class), merchantAccNo,
					customerID);
            logger.info("Get card list from merchant: " + merchantAccNo + ", number of cards found: " + cardList.size());

			return cardList;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<Card> getCardListByCustID(String customerID) {
		try {
			String sql = "SELECT C.*, M.companyName FROM Card C, Merchant M "
					+ "WHERE C.merchantAccNo=M.merchantAccNo AND C.customerID = ? ORDER BY companyName DESC, cardID";

			List<Card> cardList = this.getJdbcTemplate().query(sql,
					new BeanPropertyRowMapper<Card>(Card.class), customerID);
			logger.info("Card list is: " + cardList);

			return cardList;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean updateLastUse(String merchantAccNo, String cardID,
			Timestamp mTimestamp) {
		String sql = "UPDATE Card SET lastUse = ? "
				+ "WHERE cardID = ? AND merchantAccNo = ? AND lastUse<? AND enabled=1";
		try {
			getJdbcTemplate().update(sql, mTimestamp, cardID, merchantAccNo,
					mTimestamp);

			return true;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
