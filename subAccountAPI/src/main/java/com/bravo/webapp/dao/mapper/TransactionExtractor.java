package com.bravo.webapp.dao.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.bravo.webapp.bean.OrderItem;
import com.bravo.webapp.bean.Transaction;

public class TransactionExtractor implements
		ResultSetExtractor<List<Transaction>> {

	@Override
	public List<Transaction> extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		Map<Integer, Transaction> receiptMap = new HashMap<Integer, Transaction>();
		Map<Integer, Map<String, OrderItem>> productMap = new HashMap<Integer, Map<String, OrderItem>>();
		Map<String, OrderItem> map;
		int receiptNo;
		Transaction transaction;
		String productID;
		OrderItem orderItem;
		BigDecimal bigDecimal;
		while (rs.next()) {
			receiptNo = rs.getInt("receiptNo");
			transaction = receiptMap.get(receiptNo);
			if (transaction == null) {
				transaction = new Transaction();
				receiptMap.put(receiptNo, transaction);
				map = new HashMap<String, OrderItem>();
				productMap.put(receiptNo, map);

				transaction.setReceiptNo(receiptNo);
				transaction.setCardID(rs.getString("cardID"));
				transaction.setMerchantAccNo(rs.getString("merchantAccNo"));
//				transaction.setTotalAmount(rs.getBigDecimal("totalAmount"));
				transaction.setTotalAmount(BigDecimal.ZERO);
				transaction.setTransactionId(rs.getLong("transactionID"));

			} 
//			else {
//				bigDecimal = transaction.getTotalAmount().add(
//						rs.getBigDecimal("totalAmount"));
//				transaction.setTotalAmount(bigDecimal);
//			}

			map = productMap.get(receiptNo);
			productID = rs.getString("productID");
			orderItem = map.get(productID);
			if (orderItem == null) {
				orderItem = new OrderItem();
				map.put(productID, orderItem);

				orderItem.setProductID(productID);
				orderItem.setProductName(rs.getString("productName"));
				orderItem.setTax(rs.getBigDecimal("tax"));
				orderItem.setUnit(rs.getBigDecimal("unit"));
				orderItem.setTotalPrice(rs.getBigDecimal("totalPrice"));
				orderItem.setTransactionID(transaction.getTransactionId());
				
//				bigDecimal = transaction.getTotalAmount().add(orderItem.getTotalPrice());
//				transaction.setTotalAmount(bigDecimal);

			} else {
				bigDecimal = orderItem.getUnit().add(rs.getBigDecimal("unit"));
				orderItem.setUnit(bigDecimal);
				bigDecimal = orderItem.getTotalPrice().add(
						rs.getBigDecimal("totalPrice"));
				System.out
						.println(orderItem.getTotalPrice() + " " + bigDecimal);
				orderItem.setTotalPrice(bigDecimal);
			}

		}

		Iterator<Integer> iterator = receiptMap.keySet().iterator();
		while (iterator.hasNext()) {
			receiptNo = iterator.next();
			transaction = receiptMap.get(receiptNo);
			transaction.setOrderItemList(new ArrayList<OrderItem>(productMap
					.get(receiptNo).values()));
		}

		return new ArrayList<Transaction>(receiptMap.values());
	}

}
