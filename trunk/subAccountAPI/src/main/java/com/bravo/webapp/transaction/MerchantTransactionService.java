package com.bravo.webapp.transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bravo.webapp.bean.Card;
import com.bravo.webapp.bean.OrderItem;
import com.bravo.webapp.bean.Transaction;
import com.bravo.webapp.constant.TransactionType;
import com.bravo.webapp.dao.CardDAO;
import com.bravo.webapp.dao.CustomerDAO;
import com.bravo.webapp.dao.MerchantDAO;
import com.bravo.webapp.dao.TransactionDAO;
import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.security.LoginInformation;

public class MerchantTransactionService {

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private MerchantDAO merchantDAO;

	@Autowired
	private CardDAO cardDAO;

	@Autowired
	private TransactionDAO transactionDAO;

	private int timeInterval;

	public MerchantTransactionService() {
		this(5);
	}

	public MerchantTransactionService(int timeInterval) {
		this.timeInterval = timeInterval;
	}

	@Transactional
	public Card loadCustomerMoney(String customerEmail, String location,
			Transaction transaction) {
		String merchantAccNo = LoginInformation.getMerchantAccNo();
		String customerID = LoginInformation.getCustomerID();
		if (customerID != null) {
			throw new UnknownResourceException(
					"You have no right to execute this function.");
		}

		BigDecimal totalAmount = transaction.getTotalAmount();
		transaction.setMerchantAccNo(merchantAccNo);

		// Fetch the card from Database
		customerID = customerDAO.getCustomerID(customerEmail);
		Card card = cardDAO.getPrimaryCard(merchantAccNo, customerID);
		if (card == null) {
			throw new UnknownResourceException(
					"Failed to fetch the primary card");
		}
		transaction.setCardID(card.getCardID());

		// Update customer card balance
		if (!cardDAO
				.creditBalance(merchantAccNo, card.getCardID(), totalAmount)) {
			throw new UnknownResourceException(
					"Credit customer balance failed.");
		}

		// update merchant balance
		if (!merchantDAO.creditMerchantBalance(merchantAccNo, totalAmount)) {
			throw new UnknownResourceException(
					"Credit merchant balance failed.");
		}

		if (!transactionDAO.addTransaction(transaction)) {
			throw new UnknownResourceException("Transaction failed");
		}
		// Update Card Last use time
		if (!cardDAO.updateLastUse(merchantAccNo, card.getCardID(),
				transaction.getTransactionDate())) {
			throw new UnknownResourceException("Failed to update last use date");
		}

		// Return updated card balance
		card = cardDAO.getCard(merchantAccNo, card.getCardID());
		if (card == null) {
			throw new UnknownResourceException("Can not get the card detail");
		}

		return card;

	}

	@Transactional
	public List<Transaction> getRefundTxByEmail(String customerEmail,
			Map<String, OrderItem> refundItemMap, int days) {
		if (days <= 0) {
			throw new UnknownResourceException("illegal number of days");
		}

		String merchantAccNo = LoginInformation.getMerchantAccNo();
		String customerID = LoginInformation.getCustomerID();
		if (customerID != null) {
			System.out.println("You have no right to execute this function.");
			throw new UnknownResourceException(
					"You have no right to execute this function.");
		}
		customerID = customerDAO.getCustomerID(customerEmail);

		List<Card> cardList = cardDAO.getCardList(merchantAccNo, customerID);
		if (cardList == null || cardList.isEmpty()) {
			System.out.println("Empty card list related to " + customerEmail);
			throw new UnknownResourceException("Empty card list related to "
					+ customerEmail);
		}

		List<Transaction> refundableTxList = new ArrayList<Transaction>();
		List<OrderItem> refundItemList = new ArrayList<OrderItem>(
				refundItemMap.values());
		Iterator<Card> iterator = cardList.iterator();
		Card card;
		String cardID;
		List<Transaction> tempTxList;
		while (iterator.hasNext()) {
			card = iterator.next();
			cardID = card.getCardID();
			tempTxList = getRefundTx(cardID, refundItemList, days);
			if (tempTxList != null) {
				refundableTxList.addAll(tempTxList);
			}
		}

		refundableTxList = getRefundSummary(refundableTxList, refundItemMap);

		return refundableTxList;
	}

	public List<Transaction> getRefundSummary(
			List<Transaction> refundableTxList,
			Map<String, OrderItem> refundItemMap) {
		Iterator<Transaction> iTx = refundableTxList.iterator();
		Transaction transaction;
		while (iTx.hasNext()) {
			transaction = iTx.next();
			transaction = getRefundSummary(transaction, refundItemMap);
			if (transaction.getTotalAmount().compareTo(BigDecimal.ZERO) == 0) {
				iTx.remove();
			}
		}

		return refundableTxList;
	}

	public Transaction getRefundSummary(Transaction transaction,
			Map<String, OrderItem> refundItemMap) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal totalPrice;
		Iterator<OrderItem> iterator = transaction.getOrderItemList()
				.iterator();
		OrderItem paidItem, refundItem;
		int result;
		while (iterator.hasNext()) {
			paidItem = iterator.next();
			refundItem = refundItemMap.get(paidItem.getKey());
			result = paidItem.compareTo(refundItem);
			if (result > 0) {
				totalPrice = paidItem.getTotalPrice()
						.multiply(refundItem.getUnit())
						.divide(paidItem.getUnit(), 2, RoundingMode.HALF_UP);
				paidItem.setUnit(refundItem.getUnit());
				paidItem.setTotalPrice(totalPrice);
			} else {
				paidItem.setRefundable(false);
			}
			
			if (paidItem.getTotalPrice().compareTo(BigDecimal.ZERO) == 0) {
				iterator.remove();
			}
			totalAmount = totalAmount.add(paidItem.getTotalPrice());
		}
		transaction.setTotalAmount(totalAmount);

		return transaction;
	}

	public List<Transaction> getRefundTxByCard(String cardID,
			Map<String, OrderItem> refundItemMap, int days) {
		if (days <= 0) {
			throw new UnknownResourceException("illegal number of days");
		}

		String customerID = LoginInformation.getCustomerID();
		if (customerID != null) {
			System.out.println("You have no right to execute this function.");
			throw new UnknownResourceException(
					"You have no right to execute this function.");
		}

		List<OrderItem> refundItemList = new ArrayList<OrderItem>(
				refundItemMap.values());
		List<Transaction> refundableTxList = getRefundTx(cardID,
				refundItemList, days);

		refundableTxList = getRefundSummary(refundableTxList, refundItemMap);

		return refundableTxList;
	}

	private List<Transaction> getRefundTx(String cardID,
			List<OrderItem> refundItemList, int days) {
		String merchantAccNo = LoginInformation.getMerchantAccNo();

		List<Transaction> tempTxList;
		tempTxList = transactionDAO.getTxByCard(merchantAccNo, cardID,
				refundItemList, days);
		if (tempTxList != null) {
			System.out.println("tempTxList: " + tempTxList.size());
			System.out.println(tempTxList.toString());
		}

		return tempTxList;
	}

	@Transactional
	public void refundMoneyByUnit(Transaction transaction,
			Map<String, OrderItem> refundMap, int days) {
		if (days <= 0) {
			throw new UnknownResourceException("illegal number of days");
		}
		String merchantAccNo = LoginInformation.getMerchantAccNo();
		String customerID = LoginInformation.getCustomerID();
		if (customerID != null) {
			throw new UnknownResourceException(
					"You have no right to execute this function.");
		}
		int receiptNo = transaction.getReceiptNo();
		String cardID = transaction.getCardID();

		// Retrieve the transaction according to the merchant account number and
		// the receipt number
		System.out.println("begin retrive transaction via reciptNo");
		List<Transaction> transactionList = transactionDAO.getTxByReceiptNo(
				merchantAccNo, cardID, receiptNo, refundMap.values(), days);
		// Transaction doesn't exist, throw the exception
		if (transactionList.size() == 0) {
			throw new UnknownResourceException(
					"Can't find the transaction with receiptNo: " + receiptNo);
		}

		// Calculate the purchased amount (debit the amount that had been
		// refunded before)
		System.out
				.println("Begin to calculate unit: " + transactionList.size());
		transaction = transactionList.get(0);
		transaction = getRefundSummary(transaction, refundMap);

		// Get customer card information
		System.out.println("Begin to get Card");
		Card customerCard = cardDAO.getCard(merchantAccNo,
				transaction.getCardID());
		if (customerCard == null) {
			throw new UnknownResourceException("Can not get the card");
		}

		// Credit the customer card balance.
		System.out.println("begin to credit balance");
		if (!cardDAO.creditBalance(customerCard.getMerchantAccNo(),
				customerCard.getCardID(), transaction.getTotalAmount())) {
			throw new UnknownResourceException("Can not credit balance");
		}

		// Prepare the transaction record
		System.out.println("begin to record transaction");
		long transactionID = transactionDAO.getLastTransactionID();
		System.out.println("after call: " + transaction.toString());
		System.out.println("begin to record transaction");
		transaction.setCustomerTimestamp(null);
		transaction.setTransactionType(TransactionType.GET_REFUND);
		transaction
				.setTransactionDate(new Timestamp(System.currentTimeMillis()));
		if (!transactionDAO.addTransaction(transaction)) {
			throw new UnknownResourceException("Can not add transaction.");
		}
		
		// Negate the unit and the totalPrice
		Iterator<OrderItem> iterator = transaction.getOrderItemList()
				.iterator();
		OrderItem refundItem;
		while (iterator.hasNext()) {
			refundItem = iterator.next();
			refundItem.negate();
		}

		// Add the refund information into the database
		if (!transactionDAO.addOrderItemList(transaction.getOrderItemList(),
				transactionID)) {
			throw new UnknownResourceException("Can not add order item list.");
		}
		
		// Mark the refunded item as non-refundable
		iterator = transaction.getOrderItemList().iterator();
		while (iterator.hasNext()) {
			refundItem = iterator.next();
			if (!refundItem.isRefundable()) {
				transactionDAO.disableRefund(transaction, refundItem);
			}
		}

	}

	// Check if the customer time stamp is valid
	public void checkTimestamp(Timestamp merchantTimestamp,
			Timestamp customerTimestamp) {
		Calendar customerCalendar = Calendar.getInstance();
		customerCalendar.setTime(customerTimestamp);
		Calendar merchantCalendar = Calendar.getInstance();
		merchantCalendar.setTime(merchantTimestamp);
		merchantCalendar.add(Calendar.MINUTE, -timeInterval);

		// Compare Timestamps
		if (customerCalendar.before(merchantCalendar)) {
			// Timestamp was used after "timeInterval" minutes since it was generated
			throw new UnknownResourceException("Timestamp is too old");
		}

		// Check if the merchant's timestamp is earlier than customer ones
		if (merchantTimestamp.before(customerTimestamp)) {
			throw new UnknownResourceException(
					"Merchant's timestamp too early.");
		}

	}
}
