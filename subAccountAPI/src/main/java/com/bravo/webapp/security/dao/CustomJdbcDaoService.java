package com.bravo.webapp.security.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContextException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.util.Assert;

import com.bravo.webapp.security.bean.CustomWebAuthenticationDetails;

public class CustomJdbcDaoService extends JdbcDaoImpl {
	private String defaultRole;
	private int numParams;
	private boolean alwaysAddDefaultRole = true;

	public CustomJdbcDaoService(String defaultRole, int numParams) {
		super();
		Assert.hasText(defaultRole, "Default Role must not be empty or null");
		this.defaultRole = defaultRole;
		Assert.isTrue(numParams >= 1, "Number of Parameters must >= 1");
		this.numParams = numParams;
	}

	@Override
	protected void initDao() throws ApplicationContextException {
		setEnableAuthorities(false);
		setEnableGroups(false);
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// username = prepareParams(username);
		List<UserDetails> users = loadUsersByUsername(username);
		System.out.println(username + "@loadUserByUsername");

		if (users.size() == 0) {
			logger.debug("Query returned no results for user '" + username
					+ "'");
			System.out.println("Query returned no results for user '"
					+ username + "'");
			throw new UsernameNotFoundException(
					"Query returned no results for user '" + username + "'");
		}

		UserDetails user = users.get(0); // contains no GrantedAuthority[]

		Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();

		if (getEnableAuthorities()) {
			dbAuthsSet.addAll(loadUserAuthorities(username));
			System.out.println("load Authorities from database");
		}

		if (getEnableGroups()) {
			dbAuthsSet.addAll(loadGroupAuthorities(username));
			System.out.println("load Authorities from database");
		}

		List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(
				dbAuthsSet);

		if (alwaysAddDefaultRole || dbAuths.size() == 0) {
			addCustomAuthorities(user.getUsername(), dbAuths);
			System.out.println("Add Default Authority");
		}

		if (dbAuths.size() == 0) {
			logger.debug("User '" + username
					+ "' has no authorities and will be treated as 'not found'");
			System.out
					.println("User '"
							+ username
							+ "' has no authorities and will be treated as 'not found'");
			throw new UsernameNotFoundException("User '" + username
					+ "' has no authorities and will be treated as 'not found'");
		}

		user = createUserDetails(username, user, dbAuths);
		System.out.println(user.toString());

		return user;

	}

	@Override
	protected List<UserDetails> loadUsersByUsername(String username) {
		System.out.println("Get user details: " + username);

		return getJdbcTemplate().query(getUsersByUsernameQuery(),
				separateField(username), new RowMapper<UserDetails>() {
					public UserDetails mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						String username = rs.getString(1);
						String password = rs.getString(2);
						boolean enabled = rs.getBoolean(3);
						return new User(username, password, enabled, true,
								true, true, AuthorityUtils.NO_AUTHORITIES);
					}

				});
	}

	@Override
	protected List<GrantedAuthority> loadUserAuthorities(String username) {
		return getJdbcTemplate().query(getAuthoritiesByUsernameQuery(),
				separateField(username), new RowMapper<GrantedAuthority>() {
					public GrantedAuthority mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						String roleName = getRolePrefix() + rs.getString(2);
						SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
								roleName);

						return authority;
					}
				});
	}

	protected String[] separateField(String name) {
		String[] params = name.split(CustomWebAuthenticationDetails
				.getUsernameSeparationKey());
		if (params.length == numParams) {
			return params;
		}

		String[] newParams = new String[numParams];
		for (int i = 0; i < numParams; i++) {
			newParams[i] = params[i];
		}
		
		return newParams;
	}

	@Override
	protected void addCustomAuthorities(String username,
			List<GrantedAuthority> authorities) {
		authorities.add(new SimpleGrantedAuthority(this.defaultRole));
	}

	public String getDefaultRole() {
		return defaultRole;
	}

}
