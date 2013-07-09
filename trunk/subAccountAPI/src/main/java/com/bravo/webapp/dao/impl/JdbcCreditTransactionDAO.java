package com.bravo.webapp.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.bravo.webapp.bean.CreditTransaction;
import com.bravo.webapp.dao.CreditTransactionDAO;

public class JdbcCreditTransactionDAO extends JdbcDaoSupport implements
		CreditTransactionDAO {

	@Override
	public boolean addTransaction(CreditTransaction creditTransaction) {
		String sql = "INSERT INTO CreditcardTransaction (transactionID, cardNumber, expirationDate, createDate ) "
				+ "VALUES (?, ?, ?, ?)";
		try {
			int result = getJdbcTemplate().update(sql,
					creditTransaction.getTransactionId(),
					creditTransaction.getCardNumber(),
					creditTransaction.getExpirationDate(),
					creditTransaction.getCreateDate());

			return result == 1;
		} catch (DataAccessException ex) {
			System.out.println("Credit Transaction failed!");
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

}
