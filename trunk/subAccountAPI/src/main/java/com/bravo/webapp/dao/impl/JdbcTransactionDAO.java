package com.bravo.webapp.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.bravo.webapp.bean.OrderItem;
import com.bravo.webapp.bean.Transaction;
import com.bravo.webapp.dao.TransactionDAO;
import com.bravo.webapp.dao.mapper.TransactionExtractor;
import com.bravo.webapp.dao.mapper.TransactionRowMapper;
import com.bravo.webapp.exception.UnknownResourceException;

public class JdbcTransactionDAO extends JdbcDaoSupport implements
		TransactionDAO {

	@Override
	public boolean addTransaction(Transaction transaction) {
		String sql = "INSERT INTO Transaction (totalAmount, transactionDate, note, location, transactionType, merchantAccNo, cardID, customerTimestamp, receiptNo) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			System.out.println("Transaction starting!");
			System.out.println(getJdbcTemplate().toString());
			int result = getJdbcTemplate().update(sql,
					transaction.getTotalAmount(),
					transaction.getTransactionDate(), transaction.getNote(),
					transaction.getLocation(),
					transaction.getTransactionType(),
					transaction.getMerchantAccNo(), transaction.getCardID(),
					transaction.getCustomerTimestamp(),
					transaction.getReceiptNo());
			System.out.println("Transaction commited!");

			return result == 1;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("Add Transaction failed!");
		return false;
	}

	@Override
	public Transaction getTransaction(long transactionID) {
		String sql = "SELECT * FROM Transaction WHERE transactionID = ?";

		try {
			return getJdbcTemplate().queryForObject(sql,
					new TransactionRowMapper(), transactionID);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("Get Transaction failed!");
		return null;
	}

	@Override
	public boolean isTimestampUnique(String cardID, Timestamp customerTimestamp) {
		String sql = "SELECT customerTimestamp FROM Transaction WHERE cardID=? AND customerTimestamp=?";

		Timestamp dateTime = null;

		try {
			dateTime = getJdbcTemplate().queryForObject(sql, Timestamp.class,
					cardID, customerTimestamp);
			System.out.println(dateTime + " " + customerTimestamp);

//			if (dateTime == null) {
//				return true;
//			}
		} catch (EmptyResultDataAccessException ee) {
			return true;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	@Override
	public long getLastTransactionID() {
		String sql = "SELECT Auto_increment FROM information_schema.tables WHERE table_name=?";

		try {

			return getJdbcTemplate().queryForLong(sql, "Transaction");
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return -1;
	}

	@Override
	public boolean addOrderItemList(List<OrderItem> orderItemList,
			long transactionID) {
		try {
			int result;
			for (OrderItem item : orderItemList) {
				String sql = "INSERT INTO OrderItem (transactionID, productID, productName, unit, tax, totalPrice) "
						+ "VALUES(?, ?, ?, ?, ?, ?)";
				result = getJdbcTemplate().update(sql, transactionID,
						item.getProductID(), item.getProductName(),
						item.getUnit(), item.getTax(), item.getTotalPrice());
				if (result != 1) {
					throw new UnknownResourceException("Can't insert orderItem");
				}
			}

			return true;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	@Override
	public List<Transaction> getTransactionList(String merchantAccNo,
			String cardID) {
		try {
			String sql = "SELECT * FROM Transaction "
					+ "WHERE merchantAccNo = ? AND cardID = ? ORDER BY transactionDate DESC";
			List<Transaction> transactionList = getJdbcTemplate().query(sql,
					new TransactionRowMapper(), merchantAccNo, cardID);

			return transactionList;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Transaction> getAllTransactionList(String customerID) {
		try {
			String sql = "SELECT tx.* FROM Transaction tx, Card c "
					+ "WHERE c.customerID=? AND c.merchantAccNo=tx.merchantAccNo AND c.cardID=tx.cardID "
					+ "ORDER BY transactionDate DESC;";
			List<Transaction> transactionList = getJdbcTemplate().query(sql,
					new TransactionRowMapper(), customerID);

			return transactionList;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Transaction> getTxByReceiptNo(String merchantAccNo,
			String cardID, int receiptNo,
			Collection<OrderItem> refundItemCollection, int days) {
		List<Object> params = new ArrayList<Object>();
		params.add(merchantAccNo);
		params.add(cardID);
		params.add(receiptNo);
		params.add(days);

		Iterator<OrderItem> iterator = refundItemCollection.iterator();
		OrderItem item = iterator.next();
		params.add(item.getProductID());
		StringBuilder builder = new StringBuilder("?");
		while (iterator.hasNext()) {
			builder.append(",?");
			item = iterator.next();
			params.add(item.getProductID());
		}
		System.out.println(params.toString());

		try {
			String sql = "SELECT tx.transactionID, tx.cardID, tx.merchantAccNo, tx.totalAmount, tx.receiptNo, "
					+ "oi.productID, oi.productName, oi.tax, SUM(oi.unit) AS unit, SUM(oi.totalPrice) AS totalPrice "
					+ "FROM Transaction AS tx, OrderItem AS oi "
					+ "WHERE tx.merchantAccNo=? AND tx.cardID=? AND tx.receiptNo=? AND tx.transactionID = oi.transactionID "
					+ "AND (tx.transactionDate BETWEEN TIMESTAMPADD(DAY, -?, NOW()) AND NOW()) AND oi.productID in ("
					+ builder.toString()
					+ ") AND oi.refundable=1 "
					+ "GROUP BY tx.receiptNo, oi.productID ORDER BY tx.transactionDate, oi.productID;";
			List<Transaction> transactionList = getJdbcTemplate().query(sql,
					params.toArray(), new TransactionExtractor());

			return transactionList;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Transaction> getTxByCard(String merchantAccNo, String cardID,
			List<OrderItem> refundItemList, int days) {
		List<Object> params = new ArrayList<Object>();
		params.add(cardID);
		params.add(merchantAccNo);
		params.add(days);
		params.add(refundItemList.get(0).getProductID());
		StringBuilder builder = new StringBuilder("?");
		for (int i = 1; i < refundItemList.size(); i++) {
			builder.append(",?");
			params.add(refundItemList.get(i).getProductID());
		}
		System.out.println(params.toString());

		try {
			String sql = "SELECT MIN(tx.transactionID) AS transactionID, tx.cardID, tx.merchantAccNo, SUM(tx.totalAmount) AS totalAmount, tx.receiptNo, "
					+ "oi.productID, oi.productName, oi.tax, SUM(oi.unit) AS unit, SUM(oi.totalPrice) AS totalPrice "
					+ "FROM Transaction AS tx, OrderItem AS oi "
					+ "WHERE tx.cardID=? AND merchantAccNo=? AND tx.transactionID = oi.transactionID "
					+ "AND (tx.transactionDate BETWEEN TIMESTAMPADD(DAY, -?, NOW()) AND NOW()) AND oi.productID in ("
					+ builder.toString()
					+ ") AND oi.refundable=1 "
					+ "GROUP BY tx.receiptNo, oi.productID ORDER BY tx.transactionDate, oi.productID;";
			List<Transaction> transactionList = getJdbcTemplate().query(sql,
					params.toArray(), new TransactionExtractor());

			return transactionList;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public int disableRefund(Transaction transaction, OrderItem refundItem) {
		try {
			String sql = "UPDATE Transaction AS tx, OrderItem AS oi SET refundable=0 "
					+ "WHERE tx.receiptNo=? AND tx.merchantAccNo=? AND tx.cardID=? AND oi.productID=? AND tx.transactionID=oi.transactionID;";
			int result = getJdbcTemplate().update(sql,
					transaction.getReceiptNo(), transaction.getMerchantAccNo(),
					transaction.getCardID(), refundItem.getProductID());

			return result;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;

	}

	@Override
	public int disableRefund(Transaction transaction) {
		try {
			String sql = "UPDATE Transaction AS tx, OrderItem AS oi SET refundable=0 "
					+ "WHERE tx.receiptNo=? AND tx.merchantAccNo=? AND tx.cardID=? AND tx.transactionID=oi.transactionID;";
			int result = getJdbcTemplate().update(sql,
					transaction.getReceiptNo(), transaction.getMerchantAccNo(),
					transaction.getCardID());

			return result;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;

	}

}
