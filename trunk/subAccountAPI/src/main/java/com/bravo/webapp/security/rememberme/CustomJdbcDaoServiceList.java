package com.bravo.webapp.security.rememberme;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bravo.webapp.security.MerchantAuthenticationProvider;
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

	public CustomJdbcDaoServiceList(ProviderManager providerManager, JdbcCustomerDAO customerDAO) {
		userDetailServiceMap = new HashMap<String, UserDetailsService>();
		customerMap = new HashMap<String, Boolean>();

        // Get the providers
        Iterator<AuthenticationProvider> iterator = providerManager.getProviders().iterator();

        CustomAuthenticationProvider customAuthenticationProviderProvider;
        MerchantAuthenticationProvider merchantAuthenticationProvider;

        while (iterator.hasNext()) {
            AuthenticationProvider provider = iterator.next();

            logger.log(Level.INFO, "Current authentication provider is: " + provider.toString());

            // If it is CustomerAuthenticationProvider, put role into userDetailServiceMap
            if (provider instanceof CustomAuthenticationProvider) {
                customAuthenticationProviderProvider = (CustomAuthenticationProvider) provider;
                // Update userDetailServiceMap
				userDetailServiceMap.put(customAuthenticationProviderProvider.getRoleType(), customAuthenticationProviderProvider.getUserDetailsService());
                logger.log(Level.INFO, "Put " + customAuthenticationProviderProvider.getRoleType() + " into userDetailServiceMap.");

                // If it is customer, update the customerMap
                customerMap.put(customAuthenticationProviderProvider.getRoleType(), customAuthenticationProviderProvider.isCustomerFlag());
			}

            // If it is MerchantAuthenticationProvider, put role into userDetailServiceMap
            if (provider instanceof MerchantAuthenticationProvider) {
                merchantAuthenticationProvider = (MerchantAuthenticationProvider) provider;
                userDetailServiceMap.put(merchantAuthenticationProvider.getRoleType(), merchantAuthenticationProvider.getUserDetailsService());
                logger.log(Level.INFO, "Put " + merchantAuthenticationProvider.getRoleType() + " into userDetailServiceMap.");
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
		Boolean isCustomer;
        try {
            isCustomer = customerMap.get(roleType);
        } catch (NullPointerException e) {
            // If get null pointer exception, means it is not customer
            logger.log(Level.INFO, "Is not customer");
            return false;
        }
        return isCustomer == null ? false : isCustomer;
	}

	public CustomerDAO getCustomerDAO() {
		return customerDAO;
	}

}
