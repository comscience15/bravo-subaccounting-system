package com.bravo.webapp.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.bravo.webapp.bean.Customer;
import com.bravo.webapp.bean.PaymentProfile;
import com.bravo.webapp.dao.CustomerDAO;
import com.bravo.webapp.exception.UnknownResourceException;

public class JdbcCustomerDAO extends JdbcDaoSupport implements CustomerDAO,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3819563280052430229L;

	@Override
	public String generateCustomerID() {
		String sql = "SELECT COUNT(customerID) FROM Customer";
		try {
			int count = getJdbcTemplate().queryForInt(sql);
			System.out.print(count);

			return Integer.toString(count + 1);
		} catch (DataAccessException ex) {
			System.out.print(ex.getMessage());
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		throw new UnknownResourceException("Error: @getCustomerID()");
	}

	@Override
	public boolean addCustomer(Customer customer) {
		String sql = "INSERT INTO Customer (customerID, email, password, firstName, middleInitial, lastName, street, city, state, zip, enabled) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			int result = getJdbcTemplate().update(sql, generateCustomerID(),
					customer.getEmail(), customer.getPassword(),
					customer.getFirstName(), customer.getMiddleInitial(),
					customer.getLastName(), customer.getStreet(),
					customer.getCity(), customer.getState(), customer.getZip(),
					customer.isEnabled());

			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean changePassword(String email, String password) {
		String sql = "UPDATE Customer SET password=? WHERE email=?";
		try {
			int result = getJdbcTemplate().update(sql, password, email);

			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		throw new UnknownResourceException("Change Password Failed.");
	}

	@Override
	public boolean updateCustomer(Customer customer) {
		String sql = "UPDATE Customer SET "
				+ "firstName=?, middleInitial=?, lastName=?, "
				+ "street=?, city=?, state=?, zip=?, enabled=? "
				+ "WHERE email=?";
		try {
			int result = getJdbcTemplate()
					.update(sql, customer.getFirstName(),
							customer.getMiddleInitial(),
							customer.getLastName(), customer.getStreet(),
							customer.getCity(), customer.getState(),
							customer.getZip(), customer.getEmail());

			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public Customer getCustomer(String customerID) {
		String sql = "SELECT * FROM Customer WHERE customerID=?";
		try {
			Customer customer = getJdbcTemplate().queryForObject(sql,
					new BeanPropertyRowMapper<Customer>(Customer.class),
					customerID);

			return customer;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String getCustomerID(String username) {
		String sql = "SELECT customerID FROM Customer WHERE email=?";
		try {
			System.out.println(username);
			return getJdbcTemplate()
					.queryForObject(sql, String.class, username);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			throw new UnknownResourceException(
					"Can't get customerID through email: " + username);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnknownResourceException("Database Connection error");
		}
	}

	public boolean addPaymentProfile(PaymentProfile profile) {
		String sql = "INSERT INTO PaymentProfile (name, paymentType, accountNumber, ABA, name1, name2, firstName, middleInitial, lastName, checkProfileType, expirationDate, creditCardProcessingType, street, city, state, zip, customerID) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			int result = getJdbcTemplate().update(sql, profile.getName(),
					profile.getPaymentType(), profile.getAccountNumber(),
					profile.getABA(), profile.getName1(), profile.getName2(),
					profile.getFirstName(), profile.getMiddleInitial(),
					profile.getLastName(), profile.getCheckProfileType(),
					profile.getExpirationDate(),
					profile.getCreditCardProcessingType(), profile.getStreet(),
					profile.getCity(), profile.getState(), profile.getZip(),
					profile.getCustomerID());
			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		}

		return false;
	}

	@Override
	public PaymentProfile getPaymentProfile(String name, String customerID) {
		String sql = "SELECT * FROM PaymentProfile WHERE name=? AND customerID=?";
		try {
			PaymentProfile profile = getJdbcTemplate().queryForObject(
					sql,
					new BeanPropertyRowMapper<PaymentProfile>(
							PaymentProfile.class), name, customerID);
			return profile;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	public PaymentProfile getPaymentProfile(int paymentProfileID) {
		String sql = "SELECT * FROM PaymentProfile WHERE paymentProfileID=?";
		try {
			PaymentProfile profile = this.getJdbcTemplate().queryForObject(
					sql,
					new BeanPropertyRowMapper<PaymentProfile>(
							PaymentProfile.class), paymentProfileID);
			return profile;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<PaymentProfile> getPaymentProfileList(String customerID) {
		String sql = "SELECT * FROM PaymentProfile WHERE customerID=?";
		try {
			List<PaymentProfile> profile = this.getJdbcTemplate().query(
					sql,
					new BeanPropertyRowMapper<PaymentProfile>(
							PaymentProfile.class), customerID);
			return profile;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
