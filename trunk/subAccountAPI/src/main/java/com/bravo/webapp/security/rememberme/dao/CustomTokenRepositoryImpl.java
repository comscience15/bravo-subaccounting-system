package com.bravo.webapp.security.rememberme.dao;

import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

public class CustomTokenRepositoryImpl extends JdbcTokenRepositoryImpl
		implements CustomPersistentTokenRepository {
	
	/** The default SQL used by <tt>removeUserTokens</tt> */
    public static final String DEF_REMOVE_TOKEN_SQL =
            "delete from persistent_logins where series = ?";
    
    private String removeTokensSql = DEF_REMOVE_TOKEN_SQL;
    
	@Override
	public void removeToken(String series) {
        getJdbcTemplate().update(removeTokensSql, series);

	}

}
