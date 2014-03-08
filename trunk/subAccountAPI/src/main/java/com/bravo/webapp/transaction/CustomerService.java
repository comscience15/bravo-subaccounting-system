package com.bravo.webapp.transaction;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bravo.clearTran.ClearTranAPIController;
import com.bravo.webapp.bean.Card;
import com.bravo.webapp.bean.PaymentProfile;
import com.bravo.webapp.dao.CardDAO;
import com.bravo.webapp.dao.CustomerDAO;
import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.security.LoginInformation;
import com.bravo.webapp.util.IdGenerator;

public class CustomerService {

    private static Logger logger = Logger.getLogger(CustomerService.class.getName());

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private CardDAO cardDAO;

	@Autowired
	private ClearTranAPIController clearTran;

	@Transactional
	public Card addCard(String cardID, String securityCode, boolean primaryCard) {
		String merchantAccNo = LoginInformation.getMerchantAccNo();
		String customerID = LoginInformation.getCustomerID();

		if (cardID.isEmpty()) {
			// Generate a new card for the customer
			// if he/she don't have any card associate with the particular
			// merchant
			int nCard = cardDAO.getNumCard(merchantAccNo, customerID);
			if (nCard != 0) {
				// Check if the customer already has a card associate with the
				// merchant
				throw new UnknownResourceException(
						"Can't generate a new card, because "
								+ LoginInformation.getUsername()
								+ " already has one more card.");
			}

			Card card;
			int counter = 0;
			do {
				// Generate a random cardID for the customer
				// Repeat this process until system generate a unique cardID for
				// this merchant
				card = IdGenerator.getInstance().cardGenerator(merchantAccNo,
						customerID);
				System.out.println("CardID: " + card.getCardID() + " , "
						+ card.getCardID().length());
				counter++;
				if (counter == 10) {
					throw new UnknownResourceException(
							"Failed to generate the new card, after " + counter
									+ " attempt.");
				}
			} while (!cardDAO.addCard(card));
			cardID = card.getCardID();
		}
		// Otherwise, add a physical card to the customer's account
		// with particular cardID, securityCode, and merchantAccNo
		else if (!cardDAO.updateCardOwner(merchantAccNo, customerID, cardID,
				securityCode)) {
			// if the operation is failed, throw the error exception
			throw new UnknownResourceException(
					"Invalid Card ID or Card owner already exists.");
		}

		// Update the primary card of the customer
		if (primaryCard) {
			cardDAO.updatePrimaryCard(merchantAccNo, cardID, customerID);
		}

		System.out.println("merchantAccNo: " + merchantAccNo + ", customerID: "
				+ customerID + ", cardID: " + cardID);

		Card card = cardDAO.getCard(merchantAccNo, customerID, cardID);
		if (card == null) {
			throw new UnknownResourceException("Retrieve card failed");
		}

		// if the operation is successful, return the card to the caller
		return card;
	}

	@Transactional
	public void deleteCard(String cardID, String securityCode) {
		String merchantAccNo = LoginInformation.getMerchantAccNo();
		String customerID = LoginInformation.getCustomerID();

		// Check if the customer has more than 1 card.
		if (cardDAO.getNumCard(merchantAccNo, customerID) <= 1) {
			throw new UnknownResourceException(
					"Customer has Less than 2 card for the merchant: "
							+ merchantAccNo);
		}

		// Remove the card from the customer
		Card card = cardDAO.getCard(merchantAccNo, customerID, cardID);
		cardDAO.deleteCardOwner(merchantAccNo, customerID, cardID, securityCode);

		// Update the primary card, if the removed card is the primary card of the customer
		if (card.isPrimaryCard()) {
			List<Card> cardList = cardDAO
					.getCardList(merchantAccNo, customerID);
			card = cardList.get(0);
			cardDAO.updatePrimaryCard(merchantAccNo, card.getCardID(),
					customerID);
		}
	}

	public Card getCardDetail(String cardID) {
		String merchantAccNo = LoginInformation.getMerchantAccNo();
		String customerID = LoginInformation.getCustomerID();

		return cardDAO.getCard(merchantAccNo, customerID, cardID);

	}

	public Card getPrimaryCard() {
		String merchantAccNo = LoginInformation.getMerchantAccNo();
		String customerID = LoginInformation.getCustomerID();

		return cardDAO.getPrimaryCard(merchantAccNo, customerID);
	}

	@Transactional
	public boolean updatePrimaryCard(String cardID) {
		String merchantAccNo = LoginInformation.getMerchantAccNo();
		String customerID = LoginInformation.getCustomerID();

		return cardDAO.updatePrimaryCard(merchantAccNo, cardID, customerID);
	}

	public List<Card> getCardList() {
		String merchantAccNo = LoginInformation.getMerchantAccNo();
		String customerID = LoginInformation.getCustomerID();

		return cardDAO.getCardList(merchantAccNo, customerID);
	}

	public List<Card> getCardListByCustID() {
		String customerID = LoginInformation.getCustomerID();
        logger.info("Customer ID is: " + customerID);
		logger.info("Merchant ID is: " + LoginInformation.getMerchantAccNo());
        logger.info("Login customer username is: " + LoginInformation.getUsername());

		return cardDAO.getCardListByCustID(customerID);
	}

	@Transactional
	public PaymentProfile savePaymentProfile(PaymentProfile profile) {
		clearTran.addPaymentProfile(profile);

		return customerDAO.getPaymentProfile(profile.getName(),
				profile.getCustomerID());

	}

	public List<PaymentProfile> getPaymentProfileList() {
		String customerID = LoginInformation.getCustomerID();

		return clearTran.getPaymentProfiles(customerID);
	}

	public PaymentProfile getPaymentProfile(int paymentProfileID) {
		return clearTran.getPaymentProfile(paymentProfileID);
	}

}
