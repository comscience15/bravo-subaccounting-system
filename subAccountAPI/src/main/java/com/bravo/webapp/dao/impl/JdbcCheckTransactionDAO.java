package com.bravo.webapp.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.bravo.webapp.bean.CheckTransaction;
import com.bravo.webapp.dao.CheckTransactionDAO;

public class JdbcCheckTransactionDAO extends JdbcDaoSupport implements
		CheckTransactionDAO {

	@Override
	public boolean addTransaction(CheckTransaction checkTransaction) {
		String sql = "INSERT INTO CheckTransaction (transactionID, accountNumber, ABA, checkDate) "
				+ "VALUES (?, ?, ?, ?)";
		try {
			int result = getJdbcTemplate().update(sql,
					checkTransaction.getTransactionId(),
					checkTransaction.getAccountNumber(),
					checkTransaction.getABA(), checkTransaction.getCheckDate());

			return result == 1;
		} catch (DataAccessException ex) {
			System.out.println("Check Transaction failed!");
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

}
