package com.bravo.webapp.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bravo.webapp.bean.DecryptedInfo;
import com.bravo.webapp.util.Encryption;
import com.bravo.webapp.util.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bravo.webapp.bean.Card;
import com.bravo.webapp.bean.PaymentProfile;
import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.security.LoginInformation;
import com.bravo.webapp.transaction.CustomerService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/customer/account")
public class CustomerController {

    private Logger logger = Logger.getLogger(CustomerController.class.getName());

	@Autowired
	private CustomerService customerService;

    @Autowired
    private Encryption encryption;

	// Add a new card to the customer's account for particular merchantAccNo
	@RequestMapping(method = RequestMethod.POST, value = "/addCard", produces = "application/json")
	public @ResponseBody
	Card addCard(@RequestParam String cardID,
			@RequestParam String securityCode, @RequestParam boolean primaryCard) {
		System.out.println("Add Card");

		return customerService.addCard(cardID, securityCode, primaryCard);
	}

	// Delete the card from the customer's account
	@RequestMapping(method = RequestMethod.POST, value = "/deleteCard")
	public @ResponseBody
	String deleteCard(@RequestParam String cardID,
			@RequestParam String securityCode) {

		customerService.deleteCard(cardID, securityCode);

		return "{\"susccess\":true}";
	}

	// Retrieve the card detail information of the customer for particular merchantAccNo
	@RequestMapping(method = RequestMethod.POST, value = "/getCardDetail", produces = "application/json")
	public @ResponseBody
	Card getCardDetail(@RequestParam String cardID) {

		Card card = customerService.getCardDetail(cardID);
		if (card == null) {
			throw new UnknownResourceException("Can not get the card.");
		}

		return card;
	}

	// Retrieve the primary card of the customer for particular merchantAccNo
	@RequestMapping(method = RequestMethod.POST, value = "/getPrimaryCard", produces = "application/json")
	public @ResponseBody
	Card getPrimaryCard() {

		Card card = customerService.getPrimaryCard();
		if (card == null) {
			throw new UnknownResourceException("Can not get the card.");
		}

		return card;
	}

	// Change the primary card of the customer for particular merchantAccNo
	@RequestMapping(method = RequestMethod.POST, value = "/updatePrimaryCard", produces = "application/json")
	public @ResponseBody
	Card updatePrimaryCard(String cardID) {
		if (!customerService.updatePrimaryCard(cardID)) {
			throw new UnknownResourceException(
					"Can not update the primary card.");
		}

		return getPrimaryCard();
	}

	// Get the cards of the customer for particular merchantAccNo
	@RequestMapping(method = RequestMethod.POST, value = "/getCardList", produces = "application/json")
	public @ResponseBody
	Hashtable<String, Object> getCardList() {

		List<Card> cardList = customerService.getCardList();
        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        if (cardList.isEmpty()) {
            hashtable.put("status", "204");
            hashtable.put("message", "Can not get card list");
            //throw new UnknownResourceException("Can not get the card list");
        } else if(cardList == null) {
            hashtable.put("status", "404");
            hashtable.put("message", "Can not get card list");
        } else {
            hashtable.put("status", "200");
            hashtable.put("message", cardList);
        }
		return hashtable;
	}

	// Get all cards belong to the customer
	@RequestMapping(method = RequestMethod.POST, value = "/getCardListByCustID", produces = "application/json")
	public @ResponseBody
	Hashtable<String, Object> getCardListByCustID() {
        logger.log(Level.INFO, "Get card list by customer ID");

		List<Card> cardList = customerService.getCardListByCustID();
        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
		if (cardList.isEmpty()) {
            hashtable.put("status", "204");
            hashtable.put("message", "Can not get card list");
			//throw new UnknownResourceException("Can not get the card list");
		} else if(cardList == null) {
            hashtable.put("status", "404");
            hashtable.put("message", "Can not get card list");
        } else {
            hashtable.put("status", "200");
            hashtable.put("message", cardList);
        }
		return hashtable;
	}

	// Save a new payment profile of the customer
	@RequestMapping(method = RequestMethod.POST, value = "/savePaymentProfile", produces = "application/json")
	public @ResponseBody
	PaymentProfile savePaymentProfile(@ModelAttribute PaymentProfile profile) {
		PaymentProfile newPaymentProfile = customerService
				.savePaymentProfile(profile);

		if (newPaymentProfile == null) {
			throw new UnknownResourceException(
					"Can not save the payment profile");
		}

		return newPaymentProfile;
	}

	// Get all payment profiles that customer stored in the ClearTran
	@RequestMapping(method = RequestMethod.POST, value = "/getPaymentProfileList", produces = "application/json")
	public @ResponseBody
	List<PaymentProfile> getPaymentProfileList() {
		List<PaymentProfile> profileList = customerService
				.getPaymentProfileList();
		if (profileList == null || profileList.isEmpty()) {
			throw new UnknownResourceException(
					"Can not get the payment profile list");
		}

		List<PaymentProfile> resultList = new ArrayList<PaymentProfile>();
		Iterator<PaymentProfile> iterator = profileList.iterator();

		// Return saved profiles, which are the profile's Name is not equal to
		// profile's payment type + accountNumber + ":" + customerID
		PaymentProfile profile;
		while (iterator.hasNext()) {
			profile = iterator.next();
			if (!profile.getName().equals(
					profile.getPaymentType() + profile.getAccountNumber() + ":"
							+ profile.getCustomerID())) {
				resultList.add(profile);
			}
		}

		return resultList;
	}

	// Get a particular payment profile of the customer by its paymentProfileID
	@RequestMapping(method = RequestMethod.POST, value = "/getPaymentProfile", produces = "application/json")
	public @ResponseBody
	PaymentProfile getPaymentProfile(@RequestParam int paymentProfileID) {
		PaymentProfile paymentProfile = customerService
				.getPaymentProfile(paymentProfileID);
		if (paymentProfile == null) {
			throw new UnknownResourceException(
					"Can not get the payment profile");
		}

		String customerID = LoginInformation.getCustomerID();
		if (!paymentProfile.getCustomerID().equals(customerID)) {
			throw new UnknownResourceException(
					"The payment profile is not belong to the customer.");
		}

		return paymentProfile;
	}

}
