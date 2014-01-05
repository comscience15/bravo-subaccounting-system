package com.bravo.webapp.security.rememberme;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bravo.webapp.dao.CustomerDAO;
import com.bravo.webapp.dao.impl.JdbcCustomerDAO;
import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.security.CustomAuthenticationProvider;

public class CustomJdbcDaoServiceList implements UserDetailsService {
	private Map<String, UserDetailsService> userDetailServiceMap;
	private Map<String, Boolean> customerMap;
	private CustomerDAO customerDAO;
    private Logger logger = Logger.getLogger(CustomJdbcDaoServiceList.class.getName());

	public CustomJdbcDaoServiceList(ProviderManager providerManager,
			JdbcCustomerDAO customerDAO) {
		userDetailServiceMap = new HashMap<String, UserDetailsService>();
		customerMap = new HashMap<String, Boolean>();
		Iterator<AuthenticationProvider> iterator = providerManager
				.getProviders().iterator();
		AuthenticationProvider provider;
		CustomAuthenticationProvider customProvider;
		while (iterator.hasNext()) {
			provider = iterator.next();
			if (provider instanceof CustomAuthenticationProvider) {
				customProvider = (CustomAuthenticationProvider) provider;
				userDetailServiceMap.put(customProvider.getRoleType(),
						customProvider.getUserDetailsService());
				customerMap.put(customProvider.getRoleType(),
						customProvider.isCustomerFlag());
                logger.log(Level.INFO, "userDetailServiceMap put role type is: " + customProvider.getRoleType());
			}
		}

		this.customerDAO = customerDAO;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		System.out.println(username + " @CustomJdbcDaoServiceList");
		String[] params = username.split(":", 2);
		UserDetailsService userDetailsService = userDetailServiceMap
				.get(params[0]);
		if (userDetailsService == null) {
			throw new UnknownResourceException("Wrong role type");
		}

		return userDetailsService.loadUserByUsername(params[1]);
	}

	public boolean isCustomer(String roleType) {
		Boolean result = customerMap.get(roleType);
		if (result == null) {
			throw new UnknownResourceException("Wrong role type");
		}

		return result;
	}

	public CustomerDAO getCustomerDAO() {
		return customerDAO;
	}

}
