package com.bravo.webapp.dao.impl;

import java.math.BigDecimal;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.bravo.webapp.bean.Merchant;
import com.bravo.webapp.bean.MerchantLogin;
import com.bravo.webapp.dao.MerchantDAO;
import com.bravo.webapp.exception.UnknownResourceException;

public class JdbcMerchantDAO extends JdbcDaoSupport implements MerchantDAO {

	@Override
	public boolean addMerchant(Merchant merchant) {
		String sql = "INSERT INTO Merchant (merchantAccNo, companyName, companyAddress, balance) "
				+ "VALUES (?, ?, ?, ?)";
		try {
			getJdbcTemplate().update(sql, merchant.getMerchantAccNo(),
					merchant.getCompanyName(), merchant.getCompanyAddress(),
					merchant.getBalance());
			return true;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean creditMerchantBalance(String merchantAccNo,
			BigDecimal balance) {
		if (balance.compareTo(BigDecimal.ZERO) < 0) {
			throw new UnknownResourceException("Balance can not be negative.");
		}

		String sql = "UPDATE Merchant SET balance = (balance + ?) WHERE merchantAccNo = ?";
		try {
			int result = getJdbcTemplate().update(sql, balance, merchantAccNo);
			if (result != 1) {
				throw new UnknownResourceException(
						"No data effect: credit Merchant Balance failed.");
			}

			return true;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean debitMerchantBalance(String accountNo, BigDecimal balance) {
		if (balance.compareTo(BigDecimal.ZERO) < 0) {
			throw new UnknownResourceException("Balance can not be negative.");
		}

		String sql = "UPDATE Merchant SET balance = (balance - ?) WHERE merchantAccNo = ?";
		try {
			int result = getJdbcTemplate().update(sql, balance, accountNo);
			if (result != 1) {
				throw new UnknownResourceException(
						"No data effect: debit Merchant Balance failed.");
			}

			return true;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public Merchant getMerchant(String merchantAccNo) {
		String sql = "SELECT * FROM Merchant WHERE merchantAccNo = ?";
		try {
			Merchant merchant = getJdbcTemplate().queryForObject(sql,
					new BeanPropertyRowMapper<Merchant>(Merchant.class),
					merchantAccNo);
			
			return merchant;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean addMerchantLogin(MerchantLogin account) {
		String sql = "INSERT INTO MerchantLogin (merchantAccNo, email, password) "
				+ "VALUES (?, ?, ?)";
		try {
			int result = getJdbcTemplate().update(
					sql, account.getMerchant().getMerchantAccNo(),
							account.getEmail(), account.getPassword());
			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean updateMerchantLoginEnabled(String merchantAccNo,
			String email, boolean enabled) {
		String sql = "UPDATE MerchantLogin SET enabled = ? WHERE merchantAccNo = ? AND email = ?";
		try {
			int result = getJdbcTemplate().update(sql,enabled, merchantAccNo, email);
			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean updateMerchantLoginPassword(String merchantAccNo,
			String email, String password) {
		String sql = "UPDATE MerchantLogin SET password = ? WHERE merchantAccNo = ? AND email = ?";
		try {
			getJdbcTemplate().update(sql,
					new Object[] { password, merchantAccNo, email });
			return true;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
