package com.bravo.webapp.security.rememberme.dao;

import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

public interface CustomPersistentTokenRepository extends
		PersistentTokenRepository {

	void removeToken(String series);
	
}
