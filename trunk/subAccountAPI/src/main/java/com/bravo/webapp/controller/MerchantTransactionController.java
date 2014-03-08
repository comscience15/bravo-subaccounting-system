package com.bravo.webapp.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.bravo.webapp.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bravo.webapp.bean.Card;
import com.bravo.webapp.bean.DecryptedInfo;
import com.bravo.webapp.bean.OrderItem;
import com.bravo.webapp.bean.Transaction;
import com.bravo.webapp.constant.TransactionType;
import com.bravo.webapp.transaction.CheckoutService;
import com.bravo.webapp.transaction.MerchantTransactionService;
import com.bravo.webapp.util.CollectionUtil;
import com.bravo.webapp.util.Encryption;

/**
 * @author Daniel
 */
@Controller
@RequestMapping("/merchant/transaction")
public class MerchantTransactionController {

    private Logger logger = Logger.getLogger(MerchantTransactionController.class.getName());

	@Autowired
	private CheckoutService checkoutService;

	@Autowired
	private MerchantTransactionService merchantTxService;

	@Autowired
	private Encryption encryption;

	// Load money into customer's card
	@RequestMapping(method = RequestMethod.POST, value = "/loadCustomerMoney")
	public @ResponseBody
	String loadCustomerMoney(@RequestParam String customerEmail,
			@RequestParam String location, @RequestParam BigDecimal totalAmount) {
        logger.info("Load customer money");
		// Prepare the transaction record
		Timestamp transactionDate = new Timestamp(System.currentTimeMillis());
		Transaction transaction = new Transaction();
		transaction.setCustomerTimestamp(null);
		transaction.setLocation(location);
		transaction.setNote(null);
		transaction.setTotalAmount(totalAmount);
		transaction.setTransactionDate(transactionDate);
		transaction.setTransactionType(TransactionType.CASH_REFILL);

		// Perform the load money
		Card card = merchantTxService.loadCustomerMoney(customerEmail,
				location, transaction);

		return MessageFormat.format(Global.SUCCESS_RESPONSE, card.getBalance());//"{\"balance\":\"" + card.getBalance() + "\"}";

	}

	// Get the refundable receipts from the customer email and refund item list
	@RequestMapping(method = RequestMethod.POST, value = "/getRefundTxByEmail", produces = "application/json")
	public @ResponseBody
	List<Transaction> getRefundTxByEmail(@RequestParam String customerEmail,
			@ModelAttribute Transaction transaction, @RequestParam int days) {
		// Check the refund item list, if there are duplicate productID
		// If though, sum them up.
        logger.info("Get refund transaction by email");

		// Union the refund items
		// Combine the same productID into the same record
		List<OrderItem> refundItemList = transaction.getOrderItemList();
		Map<String, OrderItem> refundItemMap = CollectionUtil
				.uniqueListToMap(refundItemList);
        logger.info("Refund items are: " + transaction.toString());

		// Perform to get the refundable receipts from the customer email and refund item list
		List<Transaction> refundableTxList = merchantTxService
				.getRefundTxByEmail(customerEmail, refundItemMap, days);
        logger.info("Number of transactions are found: " + refundableTxList.size());
        logger.info("Transaction list: " + refundableTxList.toString());

		return refundableTxList;
	}

	// Get the refundable receipts from the customer card and refund item list
	@RequestMapping(method = RequestMethod.POST, value = "/getRefundTxByCard", produces = "application/json")
	public @ResponseBody
	List<Transaction> getRefundTxByCard(@RequestParam String cardID,
			@ModelAttribute Transaction transaction, @RequestParam int days) {
        logger.info("Get refund transaction by cardID");

		// Union the refund items
		// Combine the same productID into the same record
		List<OrderItem> refundItemList = transaction.getOrderItemList();
		Map<String, OrderItem> refundItemMap = CollectionUtil
				.uniqueListToMap(refundItemList);
        logger.info("Refund items are: " + transaction.toString());

		// Perform to get the refundable receipts from the customer card and refund item list
		List<Transaction> refundableTxList = merchantTxService.getRefundTxByCard(
				cardID, refundItemMap, days);
        logger.info("Number of transactions are found: " + refundableTxList.size());
        logger.info("Transaction list: " + refundableTxList.toString());

		return refundableTxList;
	}

	// Perform the refund through the receipt and refund item list
	@RequestMapping(method = RequestMethod.POST, value = "/refundMoney/receiptNo", produces = "application/json")
	public @ResponseBody
	Transaction refundMoneyByUnit(@ModelAttribute Transaction transaction,
			@RequestParam int days) {
		int receiptNo = transaction.getReceiptNo();
        logger.info("Refund money by receipt number: " + receiptNo);

		// Union the refund items
		// Combine the same productID into the same record
		List<OrderItem> refundItemList = transaction.getOrderItemList();
		Map<String, OrderItem> refundMap = CollectionUtil
				.uniqueListToMap(refundItemList);
        logger.info("Refund items are: " + transaction.toString());

		// Perform the refund through the receipt and refund item list
        logger.info("Before doing refund: " + transaction.toString());
		merchantTxService.refundMoneyByUnit(transaction, refundMap, days);
        logger.info("After doing refund " + transaction.toString() );

		return transaction;

	}

	// Perform the purchase the items
	@RequestMapping(method = RequestMethod.POST, value = "/purchaseItems", produces = "application/json")
	public @ResponseBody
	Hashtable<String, Object> purchaseItems(HttpServletRequest request,
			@RequestParam String encryptedInfo,
			@RequestParam long merchantTimestamp,
			@ModelAttribute Transaction transaction) {

        logger.info(transaction.toString());
		logger.info("Merchant time stamp: "+merchantTimestamp);
        logger.info("Encrypted info from request is: "+encryptedInfo);

		Timestamp mTimestamp = new Timestamp(merchantTimestamp);

		// Decrypt customer card number and timestamp\
		String path = "/WEB-INF";
		DecryptedInfo decryptedInfo = encryption.infoDecryption(request, path,
				encryptedInfo);

		String cardID = decryptedInfo.getCardID();
		Timestamp customerTimestamp = decryptedInfo.getCustomerTimestamp();

		// Check if the timestamp is valid
		merchantTxService.checkTimestamp(mTimestamp, customerTimestamp);

		// Union the purchase item list
		// combine the same productID into one record
		List<OrderItem> orderItemList = transaction.getOrderItemList();
		Map<String, OrderItem> orderMap = CollectionUtil
				.uniqueListToMap(orderItemList);
		orderItemList = new ArrayList<OrderItem>(orderMap.values());

		// Prepare the transaction record
		transaction.setCardID(cardID);
		transaction.setCustomerTimestamp(customerTimestamp);
		transaction.setTransactionDate(mTimestamp);
		transaction.setTransactionType(TransactionType.PURCHASE_ITEM);
		transaction.setOrderItemList(orderItemList);

		// Perform the purchase
		transaction = checkoutService.checkoutItems(cardID, transaction);

        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        hashtable.put("status", "200");
        hashtable.put("message", transaction);

		return hashtable;

	}

}
