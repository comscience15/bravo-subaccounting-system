package com.bravo.webapp.security.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private Logger logger = Logger.getLogger(CustomJdbcDaoService.class.getName());

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
		List<UserDetails> users = loadUsersByUsername(username);

		if (users.size() == 0) {
			logger.log(Level.INFO , MessageFormat.format("Query returned no results for user: {0}", username));
			throw new UsernameNotFoundException(
					"Query returned no results for user '" + username + "'");
		}

		UserDetails user = users.get(0); // contains no GrantedAuthority[]

		Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();

		if (getEnableAuthorities()) {
			dbAuthsSet.addAll(loadUserAuthorities(username));
            logger.log(Level.INFO, "Load authorities from database");
		}

		if (getEnableGroups()) {
			dbAuthsSet.addAll(loadGroupAuthorities(username));
            logger.log(Level.INFO, "Load authorities from database");
		}

		List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(
				dbAuthsSet);

		if (alwaysAddDefaultRole || dbAuths.size() == 0) {
			addCustomAuthorities(user.getUsername(), dbAuths);
			System.out.println("Add Default Authority");
		}

		if (dbAuths.size() == 0) {
            String msg = MessageFormat.format("User {0} has no authorities and will be treated as 'not found'", username);
			logger.log(Level.WARNING, msg);
			throw new UsernameNotFoundException(msg);
		}

		user = createUserDetails(username, user, dbAuths);
		System.out.println(user.toString());

		return user;

	}

	@Override
	protected List<UserDetails> loadUsersByUsername(String username) {
        List<UserDetails> listUserDetails = getJdbcTemplate().query(getUsersByUsernameQuery(),
                separateField(username), new RowMapper<UserDetails>() {
            public UserDetails mapRow(ResultSet rs, int rowNum) {
                String username = null;
                String password = null;
                boolean enabled = false;
                try{
                    username = rs.getString(1);
                    password = rs.getString(2);
                    enabled = rs.getBoolean(3);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return new User(username, password, enabled, true,
                        true, true, AuthorityUtils.NO_AUTHORITIES);
            }

        });
		return listUserDetails;
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

        for ( String i : newParams) {
            System.out.print(i + " ");
        }
        System.out.println();
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
