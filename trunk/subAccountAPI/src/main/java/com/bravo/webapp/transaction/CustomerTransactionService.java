package com.bravo.webapp.transaction;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bravo.clearTran.ClearTranAPIController;
import com.bravo.webapp.bean.Card;
import com.bravo.webapp.bean.CheckTransaction;
import com.bravo.webapp.bean.CreditTransaction;
import com.bravo.webapp.bean.Customer;
import com.bravo.webapp.bean.PaymentProfile;
import com.bravo.webapp.bean.Transaction;
import com.bravo.webapp.constant.TransactionType;
import com.bravo.webapp.dao.CardDAO;
import com.bravo.webapp.dao.CheckTransactionDAO;
import com.bravo.webapp.dao.CreditTransactionDAO;
import com.bravo.webapp.dao.CustomerDAO;
import com.bravo.webapp.dao.MerchantDAO;
import com.bravo.webapp.dao.TransactionDAO;
import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.security.LoginInformation;

public class CustomerTransactionService {

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private CardDAO cardDAO;

	@Autowired
	private TransactionDAO transactionDAO;

	@Autowired
	private CreditTransactionDAO creditTransactionDAO;

	@Autowired
	private CheckTransactionDAO checkTransactionDAO;

	@Autowired
	private MerchantDAO merchantDAO;

	@Autowired
	private ClearTranAPIController clearTran;

	public PaymentProfile getPaymentProfile(PaymentProfile paymentProfile) {
		String customerID = LoginInformation.getCustomerID();

		PaymentProfile tempProfile = clearTran.getPaymentProfile(paymentProfile
				.getPaymentProfileID());
		if (!tempProfile.getCustomerID().equals(customerID)) {
			throw new UnknownResourceException(
					"Payment profile is not belong to the customerID: "
							+ paymentProfile.getCustomerID());
		}

		return tempProfile;
	}

	@Transactional
	public BigDecimal loadMoney(String cardID, Transaction transaction,
			PaymentProfile paymentProfile, boolean existProfile) {
		String merchantAccNo = LoginInformation.getMerchantAccNo();
		String customerID = LoginInformation.getCustomerID();
		transaction.setMerchantAccNo(merchantAccNo);

		System.out.println(cardID);
		Card card = cardDAO.getCard(merchantAccNo, customerID, cardID);
		if (card == null) {
			throw new UnknownResourceException("Failed to retrieve card");
		}

		if (!existProfile) {
			paymentProfile.setCustomerID(customerID);
			if (paymentProfile.getName().equals(
					paymentProfile.getPaymentType()
							+ paymentProfile.getAccountNumber())) {
				String name = paymentProfile.getName() + ":" + customerID;
				paymentProfile.setName(name);
				try {
					clearTran.addPaymentProfile(paymentProfile);
				} catch (UnknownResourceException ure) {
					System.out.println("Duplicate name.");
					System.out.println(ure.getMessage());
				}

			} else {
				clearTran.addPaymentProfile(paymentProfile);
			}
		}

		// Simulate add transaction into ClearTran
		if (paymentProfile.getPaymentType().equals("C")) {
			clearTran.addCreditCardTransaction(paymentProfile);
		} else {
			clearTran.addCheckTransaction(paymentProfile);
		}

		// Load money to the card
		if (!cardDAO.creditBalance(card.getMerchantAccNo(), card.getCardID(),
				transaction.getTotalAmount())) {
			throw new UnknownResourceException(
					"Failed to credit customer balance");
		}
		
		// Update Card Last use time
		if (!cardDAO.updateLastUse(card.getMerchantAccNo(), card.getCardID(),
				transaction.getTransactionDate())) {
			throw new UnknownResourceException("Failed to update last use date");
		}
		
		// Credit merchant's balance
		if (!merchantDAO.creditMerchantBalance(merchantAccNo,
				transaction.getTotalAmount())) {
			throw new UnknownResourceException(
					"Failed to credit merchant balance");
		}

		// Add transaction record to the database
		transactionDAO.addTransaction(transaction);
		long transactionId = transactionDAO.getLastTransactionID();
		transaction.setTransactionId(transactionId);
		if (transaction.getTransactionType().equals(
				TransactionType.CREDIT_CARD_REFILL)) {
			// Log credit card transaction
			creditTransactionDAO
					.addTransaction((CreditTransaction) transaction);
		} else if (transaction.getTransactionType().equals(
				TransactionType.CHECK_REFILL)) {
			// Log check transaction
			checkTransactionDAO.addTransaction((CheckTransaction) transaction);
		}

		// Retrieve updated balance
		card = cardDAO.getCard(card);
		if (card == null) {
			throw new UnknownResourceException("Can not retrieve the card.");
		}

		return card.getBalance();
	}

	@Transactional
	public BigDecimal sendGift(String senderCardID, Transaction senderTx,
			String receiverCardID, Transaction receiverTx, BigDecimal amount) {
		String senderID = LoginInformation.getCustomerID();
		String merchantAccNo = LoginInformation.getMerchantAccNo();
		senderTx.setMerchantAccNo(merchantAccNo);
		receiverTx.setMerchantAccNo(merchantAccNo);

		// Get sender card information
		// and check if the card is belong to sender.
		System.out.println("Begin get card");
		Card senderCard = cardDAO
				.getCard(merchantAccNo, senderID, senderCardID);
		if (senderCard == null) {
			throw new UnknownResourceException(senderID
					+ " is not the card owner.");
		}
		// Check if card is enabled
		if (!senderCard.isEnabled()) {
			throw new UnknownResourceException("Card is disabled");
		}
		// Check if the sender's card balance is enough.
		if (senderCard.getBalance().compareTo(amount) < 0) {
			throw new UnknownResourceException(senderCardID
					+ " does not contain enough money.");
		}
		Card receiverCard = cardDAO.getCard(merchantAccNo, receiverCardID);
		if (receiverCard == null) {
			throw new UnknownResourceException(
					"Can not retrieve the receiver's card.");
		}
		// Check if card is enabled
		if (!receiverCard.isEnabled()) {
			throw new UnknownResourceException("Card is disabled");
		}

		// Debit sender's card
		System.out.println("debit balance");
		cardDAO.debitBalance(merchantAccNo, senderCardID, amount);
		// Credit receiver's card
		System.out.println("credit balance");
		cardDAO.creditBalance(merchantAccNo, receiverCardID, amount);

		// Record the transactions
		System.out.println("record transaction");
		transactionDAO.addTransaction(senderTx);
		transactionDAO.addTransaction(receiverTx);

		// Retrieve updated balance
		senderCard = cardDAO.getCard(merchantAccNo, senderID, senderCardID);
		if (senderCard == null) {
			throw new UnknownResourceException(senderCardID
					+ " can't be retrieved.");
		}

		return senderCard.getBalance();

	}

	public List<Transaction> getTransactionList(String cardID) {
		String merchantAccNo = LoginInformation.getMerchantAccNo();
		System.out.println(merchantAccNo);
		List<Transaction> txList = transactionDAO.getTransactionList(
				merchantAccNo, cardID);
		if (txList == null) {
			throw new UnknownResourceException("Can not fund the transactions");
		}

		return txList;
	}

	public List<Transaction> getAllTransactionList() {
		String customerID = LoginInformation.getCustomerID();
		System.out.println("customerID: " + customerID);
		List<Transaction> txList = transactionDAO
				.getAllTransactionList(customerID);
		if (txList == null) {
			throw new UnknownResourceException("Can not fund the transactions");
		}

		return txList;
	}

	public String getPrimaryCardID(String receiverEmail) {
		String merchantAccNo = LoginInformation.getMerchantAccNo();

		Card card = cardDAO.getPrimaryCard(merchantAccNo,
				customerDAO.getCustomerID(receiverEmail));
		return card.getCardID();
	}

	public void setBillingAddr(PaymentProfile paymentProfile) {
		String customerID = LoginInformation.getCustomerID();

		Customer customer = customerDAO.getCustomer(customerID);
		paymentProfile.setStreet(customer.getStreet());
		paymentProfile.setCity(customer.getCity());
		paymentProfile.setState(customer.getState());
		paymentProfile.setZip(customer.getZip());

		if (paymentProfile.useAccAddr()) {
			throw new UnknownResourceException(
					"Missing fields in the billing address when copying from customer profile.");
		}
	}
}
