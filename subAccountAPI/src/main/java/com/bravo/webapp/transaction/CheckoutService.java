package com.bravo.webapp.transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.bravo.webapp.bean.Card;
import com.bravo.webapp.bean.Transaction;
import com.bravo.webapp.dao.CardDAO;
import com.bravo.webapp.dao.TransactionDAO;
import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.security.LoginInformation;

public class CheckoutService {

	@Autowired
	private CardDAO cardDAO;

	@Autowired
	private TransactionDAO transactionDAO;

	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
	public Transaction checkoutItems(String cardID, Transaction transaction) {

		String merchantAccNo = LoginInformation.getMerchantAccNo();
		transaction.setMerchantAccNo(merchantAccNo);
		BigDecimal totalAmount = transaction.getTotalAmount();
		transaction.setTotalAmount(totalAmount.negate());
		Timestamp merchantTimestamp = transaction.getTransactionDate();

		// Get the card information
		Card card = cardDAO.getCard(merchantAccNo, cardID);
		if (card == null) {
			throw new UnknownResourceException("Failed to fetch the card.");
		}

		// Check if card is enabled
		if (!card.isEnabled()) {
			throw new UnknownResourceException("Card is disabled");
		}

		String customerID = LoginInformation.getCustomerID();
		// Check if customer is the card owner.
		if (customerID != null) {
			if (!card.getCustomerID().equals(customerID)) {
				throw new UnknownResourceException("Wrong card owner.");
			}
		} else {
			Timestamp customerTimestamp = transaction.getCustomerTimestamp();
			// Check if customer timestamp was used before
			if (!transactionDAO.isTimestampUnique(cardID, customerTimestamp)) {
				throw new UnknownResourceException("Timestamp is not unique");
			}

		}

		// Check if balance is sufficient
		if (card.getBalance().compareTo(transaction.getTotalAmount()) < 0) {
			throw new UnknownResourceException("Insufficient balance");
		}

		// Update Customer Card balance
		cardDAO.debitBalance(merchantAccNo, cardID, totalAmount);

		// Update Card Last use time
		if (!cardDAO.updateLastUse(merchantAccNo, cardID, merchantTimestamp)) {
			throw new UnknownResourceException("Failed to update last use date");
		}

		// Add transaction record
		long lastTransactionID = transactionDAO.getLastTransactionID();
		System.out.println("transactionID: " + lastTransactionID);
		Integer receiptNo = (int) lastTransactionID;
		transaction.setReceiptNo(receiptNo);
		System.out.println("receiptNo: " + receiptNo);
		if (!transactionDAO.addTransaction(transaction)) {
			throw new UnknownResourceException("Add transaction failed");
		}

		// Add purchased item records
		if (!transactionDAO.addOrderItemList(transaction.getOrderItemList(),
				lastTransactionID)) {
			throw new UnknownResourceException("Fail to add ordered items.");
		}

		// Retrieve the transaction just added
		transaction = transactionDAO.getTransaction(lastTransactionID);
		if (transaction == null) {
			throw new UnknownResourceException("Get transaction failed.");
		}

		return transaction;
	}

}
