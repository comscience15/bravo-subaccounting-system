package com.bravo.webapp.security.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bravo.webapp.bean.Customer;
import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.security.LoginInformation;
import com.bravo.webapp.security.bean.LoginStatusBean;
import com.bravo.webapp.transaction.LoginService;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/authentication")
public class LoginController {
    private final Logger logger = Logger.getLogger(LoginController.this.getClass().getName());
	@Autowired
	private LoginService loginService;

    public LoginController() {
    }

    @RequestMapping(value = "/loginStatus")
	public @ResponseBody
	String loginStatus() {
		System.out.println("Login Status");
		return "{\"success\":true}";

	}

	@RequestMapping(value = "/loginFailed", produces = "application/json")
	public @ResponseBody
	String loginError() {

		System.out.println("Login Failed");
		throw new UnknownResourceException("Login failed.");

	}

	@RequestMapping(value = "/logout")
	public @ResponseBody
	LoginStatusBean logout() {
        // did not really logged out !!!!!!!!!!!!!!!!!!!!!!!!!!
		Authentication auth = LoginInformation.getAuthentication();
        logger.log(Level.INFO, "logout user is :" + auth.getName());
		return new LoginStatusBean(true, false, auth.getName(),
				"Logout Successful.");

	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public @ResponseBody
	String createUser(HttpServletRequest request, HttpServletResponse response) {

		// Obtain customer information from request parameters.
		Customer customer = obtainCustomerInfo(request);
		// Add new customer into the database
		if (loginService.addCustomer(customer)) {
			// Auto Login
			loginService.authenticate(request, response);

			return "{\"success\":true}";

			// Fail to add new customer into the database
		} else {
			// return new LoginStatusBean(false, false, null, "Signup failed.");
			throw new UnknownResourceException("User name exist: Signup failed");
		}

	}

	// Obtain customer information from request parameters.
	private Customer obtainCustomerInfo(HttpServletRequest request) {
		loginService.checkCustomerRole(request);

		Customer customer = new Customer();
		customer.setEmail(loginService.obtainUsername(request));
		customer.setPassword(loginService.obtainPassword(request));
		customer.setEnabled(true);
		customer.setFirstName(request.getParameter("firstName"));
		customer.setLastName(request.getParameter("lastName"));
		customer.setMiddleInitial(request.getParameter("middleInitial"));
		customer.setStreet(request.getParameter("street"));
		customer.setCity(request.getParameter("city"));
		customer.setState(request.getParameter("state"));
		customer.setZip(request.getParameter("zip"));

		return customer;
	}

	@RequestMapping(value = "/changePasswd")
	public @ResponseBody
	String changePassword(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String oldPassword,
			@RequestParam String newPassword) {

		if (loginService.changePassword(request, response, oldPassword,
				newPassword)) {
			return "{\"success\":\"true\"}";
		}

		throw new UnknownResourceException("Authentication failed.");
	}

}
