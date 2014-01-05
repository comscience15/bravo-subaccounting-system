package com.bravo.webapp.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bravo.webapp.bean.CheckTransaction;
import com.bravo.webapp.bean.CreditTransaction;
import com.bravo.webapp.bean.OrderItem;
import com.bravo.webapp.bean.PaymentProfile;
import com.bravo.webapp.bean.Transaction;
import com.bravo.webapp.constant.TransactionType;
import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.transaction.CheckoutService;
import com.bravo.webapp.transaction.CustomerTransactionService;
import com.bravo.webapp.util.CollectionUtil;
import com.bravo.webapp.util.Encryption;

@Controller
@RequestMapping("/customer/transaction")
public class CustomerTransactionController {

	@Autowired
	private CheckoutService checkoutService;

	@Autowired
	private CustomerTransactionService customerTxService;

	@Autowired
	private Encryption encryption;

    private Logger logger = Logger.getLogger(CustomerTransactionController.class.getName());

	// Load money to the customer card
	@RequestMapping(method = RequestMethod.POST, value = "/loadMoney", produces = "application/json")
	public @ResponseBody
	String loadMoney(@RequestParam String cardID,
			@RequestParam BigDecimal totalAmount,
			@ModelAttribute PaymentProfile paymentProfile,
			@RequestParam boolean saveProfile,
			@RequestParam boolean existProfile) {
		System.out.println("load money");
		System.out.println("amount: " + totalAmount);
		System.out.println("payment profile: " + paymentProfile.toString());
		System.out.println(saveProfile + ":" + existProfile);

		Timestamp transactionDate = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat checkDateFormat = new SimpleDateFormat("MMddyyyy");
		String date = checkDateFormat.format(transactionDate);

		Transaction transaction;
		// Create a new payment profile
		if (!existProfile) {
			if (paymentProfile.useAccAddr()) {
				// Use the customer register address as the billing address
				// load the register address into the payment profile
				customerTxService.setBillingAddr(paymentProfile);
			}
			// Set the name of the payment profile
			if (saveProfile) {
				String name = paymentProfile.getAccountNumber();
				if (name == null || name.isEmpty()) {
					if (paymentProfile.getPaymentType().equals("C")) {
						name = paymentProfile.getFirstName() + " "
								+ paymentProfile.getLastName() + "'s card";
					} else {
						name = paymentProfile.getName() + "'s account";
					}
				}
				name = paymentProfile.getName() + " - "
						+ name.substring(name.length() - 4);
				paymentProfile.setName(name);
			} else {
				String name = paymentProfile.getPaymentType()
						+ paymentProfile.getAccountNumber();
				paymentProfile.setName(name);
			}
		} else {
			// Retrieve the payment profile from ClearTran
			paymentProfile = customerTxService
					.getPaymentProfile(paymentProfile);
		}

		// Prepare the transaction record
		if (paymentProfile.getPaymentType().equals("C")) {
			transaction = new CreditTransaction();
			transaction.setTransactionType(TransactionType.CREDIT_CARD_REFILL);

			((CreditTransaction) transaction).setCardNumber(paymentProfile
					.getAccountNumber());
			((CreditTransaction) transaction).setExpirationDate(paymentProfile
					.getExpirationDate());
			((CreditTransaction) transaction).setCreateDate(date);

			paymentProfile.setCheckProfileType("P");
		} else if (paymentProfile.getPaymentType().equals("A")) {
			transaction = new CheckTransaction();
			transaction.setTransactionType(TransactionType.CHECK_REFILL);

			((CheckTransaction) transaction).setAccountNumber(paymentProfile
					.getAccountNumber());
			((CheckTransaction) transaction).setABA(paymentProfile.getABA());
			((CheckTransaction) transaction).setCheckDate(date);

			paymentProfile.setCreditCardProcessingType(paymentProfile
					.getExpirationDate());
		} else {
			throw new UnknownResourceException("Unknown Transaction Type: "
					+ paymentProfile.getPaymentType());
		}
		transaction.setCardID(cardID);
		transaction.setTotalAmount(totalAmount);
		transaction.setTransactionDate(transactionDate);
		transaction.setLocation(null);
		transaction.setNote(null);
		transaction.setCustomerTimestamp(null);

		// Load money into customer card and return the new card balance
		BigDecimal balance = customerTxService.loadMoney(cardID, transaction,
				paymentProfile, existProfile);
		System.out.println("{\"cardBalance\":\"" + balance + "\"}");

		return "{\"cardBalance\":\"" + balance + "\"}";

	}

	// Send Gift from customer's card to the other one's primary card
	@RequestMapping(method = RequestMethod.POST, value = "/sendGift", produces = "application/json")
	public @ResponseBody
	String sendGift(@RequestParam String senderCardID,
			@RequestParam String senderNote,
			@RequestParam String receiverEmail,
			@RequestParam BigDecimal totalAmount) {

		System.out.println("Send Gift");
		String receiverNote = "";
		if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new UnknownResourceException(
					"Total amount must greater than zero.");
		}

		String receiverCardID = customerTxService
				.getPrimaryCardID(receiverEmail);

		// Check if the card is belong to sender.
		if (senderCardID.equals(receiverCardID)) {
			throw new UnknownResourceException(
					"Sender's and receiver's card is the same.");
		}

		// Prepare sender's and receiver's transaction records
		Timestamp transactionDate = new Timestamp(System.currentTimeMillis());

		Transaction senderTx = new Transaction();
		senderTx.setCardID(senderCardID);
		senderTx.setTotalAmount(totalAmount.negate());
		senderTx.setTransactionDate(transactionDate);
		senderTx.setTransactionType(TransactionType.SEND_GIFT);
		senderTx.setNote(senderNote);

		Transaction receiverTx = new Transaction();
		receiverTx.setCardID(receiverCardID);
		receiverTx.setTotalAmount(totalAmount);
		receiverTx.setTransactionDate(transactionDate);
		receiverTx.setTransactionType(TransactionType.RECEIVE_GIFT);
		receiverTx.setNote(receiverNote);

		BigDecimal balance = customerTxService.sendGift(senderCardID, senderTx,
				receiverCardID, receiverTx, totalAmount);

		return "{\"balance\":\"" + balance + "\"}";

	}

	// View the Transactions of the particular card, and particular
	// merchantAccNo of the customer
	@RequestMapping(method = RequestMethod.POST, value = "/viewTransaction", produces = "application/json")
	public @ResponseBody
	List<Transaction> viewTransaction(@RequestParam String cardID) {
		System.out.println("View Transaction");
		System.out.println(cardID);

		List<Transaction> transactionList = customerTxService
				.getTransactionList(cardID);
		if (transactionList == null) {
			System.out.println("Can not get the transactions.");
			throw new UnknownResourceException("Can not get the transactions.");
		} else if (transactionList.isEmpty()) {
			System.out.println("No transaction found.");
			throw new UnknownResourceException("No transaction found.");
		}

		return transactionList;
	}

	// View all transactions belong to the customer
	@RequestMapping(method = RequestMethod.POST, value = "/viewAllTransaction", produces = "application/json")
	public @ResponseBody
	List<Transaction> viewAllTransaction() {
		System.out.println("View All Transaction");

		List<Transaction> transactionList = customerTxService
				.getAllTransactionList();
		if (transactionList == null) {
			System.out.println("Can not get the transactions.");
			throw new UnknownResourceException("Can not get the transactions.");
		} else if (transactionList.isEmpty()) {
			System.out.println("No transaction found.");
			throw new UnknownResourceException("No transaction found.");
		}
		System.out.println(transactionList.size());
		System.out.println(transactionList.toString());

		return transactionList;
	}

	// Get the public key for encryption
	@RequestMapping(method = RequestMethod.POST, value = "/getKey")
	public @ResponseBody
	String getKey(HttpServletRequest request) {

        logger.log(Level.INFO, "Get Key is called");

		String path = request.getSession().getServletContext()
				.getRealPath("/WEB-INF");

		String result = encryption.keyGeneration(path);

        logger.log(Level.INFO, "Key is: " + result);

		return result;
	}

	// Perform the self-checkout
	@RequestMapping(method = RequestMethod.POST, value = "/checkoutItems", produces = "application/json")
	public @ResponseBody
	Transaction checkoutItems(@RequestParam String cardID,
			@ModelAttribute Transaction transaction) {

		System.out.println("checkoutItems");
		System.out.println(transaction.toString());

		List<OrderItem> orderItemList = transaction.getOrderItemList();

		// Union the purchased item list
		Map<String, OrderItem> orderMap = CollectionUtil
				.uniqueListToMap(orderItemList);
		orderItemList = new ArrayList<OrderItem>(orderMap.values());

		// Prepare the transaction record
		transaction
				.setTransactionDate(new Timestamp(System.currentTimeMillis()));
		transaction.setTransactionType(TransactionType.SELF_CHECKOUT);
		transaction.setOrderItemList(orderItemList);

		// Perform the checkout
		transaction = checkoutService.checkoutItems(cardID, transaction);

		return transaction;

	}

}
