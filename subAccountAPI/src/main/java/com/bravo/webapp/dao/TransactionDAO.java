package com.bravo.webapp.dao;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import com.bravo.webapp.bean.OrderItem;
import com.bravo.webapp.bean.Transaction;

public interface TransactionDAO {

	public boolean addTransaction(Transaction transaction);

	public boolean isTimestampUnique(String cardID, Timestamp customerTimestamp);

	public boolean addOrderItemList(List<OrderItem> orderItemList,
			long transactionID);

	public List<Transaction> getTransactionList(String merchantAccNo,
			String cardID);

	public List<Transaction> getAllTransactionList(String customerID);

	public List<Transaction> getTxByReceiptNo(String merchantAccNo,
			String cardID, int receiptNo,
			Collection<OrderItem> refundItemCollection, int days);

	public List<Transaction> getTxByCard(String merchantAccNo, String cardID,
			List<OrderItem> refundItemList, int days);
	
	public int disableRefund(Transaction transaction, OrderItem refundItem);
	
	public int disableRefund(Transaction transaction);

	public long getLastTransactionID();

	public Transaction getTransaction(long transactionID);

}
