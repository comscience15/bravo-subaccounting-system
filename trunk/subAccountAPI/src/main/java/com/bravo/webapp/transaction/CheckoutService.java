package com.bravo.webapp.transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger(CheckoutService.class.getName());

	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
	public Transaction checkoutItems(String cardID, Transaction transaction) {

		String merchantAccNo = LoginInformation.getMerchantAccNo();
		transaction.setMerchantAccNo(merchantAccNo);
		BigDecimal totalAmount = transaction.getTotalAmount();
		transaction.setTotalAmount(totalAmount.negate());  // User purchase, the record in DB is negative
		Timestamp merchantTimestamp = transaction.getTransactionDate();

		// Get the card information
		Card card = cardDAO.getCard(merchantAccNo, cardID);
		if (card == null) {
            logger.warning("Failed to fetch the card");
			throw new UnknownResourceException("Failed to fetch the card.");
		}

		// Check if card is enabled
		if (!card.isEnabled()) {
            logger.warning("Card is disabled");
			throw new UnknownResourceException("Card is disabled");
		}

		String customerID = LoginInformation.getCustomerID();
		// Check if customer is the card owner.
		if (customerID != null) {
			if (!card.getCustomerID().equals(customerID)) {
                logger.warning("Wrong card owner");
				throw new UnknownResourceException("Wrong card owner.");
			}
		} else {
			Timestamp customerTimestamp = transaction.getCustomerTimestamp();
			// Check if customer timestamp was used before
			if (!transactionDAO.isTimestampUnique(cardID, customerTimestamp)) {
                logger.warning("Customer timestamp is incorrect");
				throw new UnknownResourceException("Timestamp is not unique");
			}
		}

		// Check if balance is sufficient
		if (card.getBalance().compareTo(totalAmount) < 0) {
            String msg =  MessageFormat.format("Insufficient balance, card balance is {0}, transaction amount is {1}",
                    new Object[] {card.getBalance(), totalAmount});
            logger.warning(msg);
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
        logger.info("transactionID: " + lastTransactionID);
		Integer receiptNo = (int) lastTransactionID;
		transaction.setReceiptNo(receiptNo);
        logger.info("receiptNo: " + receiptNo);
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
