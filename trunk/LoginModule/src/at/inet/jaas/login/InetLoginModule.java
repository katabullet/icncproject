package at.inet.jaas.login;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.log4j.Logger;

import at.inet.helper.PasswordHelper;

/**
 * Custom JAAS Login module.
 * 
 * A SQL database is used as user and password backend. A JNDI lookup is used to
 * get the database connection. The class has to be properly initialized by a
 * Map object @see InetLoginModule#initialize.
 * 
 * You can use the following snippet for the login-config.xml
 * 
 * 
 * 
 * <application-policy name="InetJaas"> <authentication> <login-module
 * code="at.inet.jaas.login.InetLoginModule" flag="required"> <module-option
 * name="DataSourceJndi">java:mdDs</module-option> <module-option
 * name="SelectStmnt">SELECT CPWD, CSALT, NVERSION, NFAILEDLOGINS FROM MDUSER
 * WHERE CUSERID = ?</module-option> <module-option name="UpdateStmnt">UPDATE
 * MDUSER SET NFAILEDLOGINS = ? WHERE CUSERID = ?</module-option> <module-option
 * name="RoleStmnt">select a.cprincipaleid from MDPRINCIPALE a inner join
 * MDUSERPRINCIPALE b on (a.cprincipaleid=b.cprincipaleid) inner join MDUSER c
 * on (c.cuserid = b.cuserid) where c.cuserid = ?</module-option> <!-- a
 * negative number disables this feature --> <module-option
 * name="AllowedFailedLogins">10</module-option> </login-module>
 * </authentication> </application-policy>
 * 
 * @author mme80
 * 
 */
public class InetLoginModule implements LoginModule {

	private static Logger logger = Logger
			.getLogger("at.inet.jaas.login.InetLoginModule");

	public static final String JNDINAME = "DataSourceJndi";
	public static final String ROLESTMNT = "RoleStmnt";

	private Subject subject;
	private CallbackHandler callbackHandler;
	// private Map<String, ?> sharedState;
	private Map<String, ?> options;

	private boolean commitSucceeded = false;
	private boolean loginSucceeded = false;

	private String username;
	private Principal user;
	ArrayList<Principal> principalList;

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		// System.out.println("Initializer " + this.toString());
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		// this.sharedState = sharedState;
		this.options = options;
		// System.out.println(options.get("AllowedFailedLogins"));
	}

	/**
	 * Returns a connection object via jndi lookup which can be used to query
	 * the database.
	 * 
	 * 
	 * @param jndiName
	 *            - the name of datasource bound in jndi
	 * @return Connection object from jndi
	 */
	public Connection getSQLConnection(String jndiName) {
		java.sql.Connection conn = null;
		try {
			Context initContext = new InitialContext();
			javax.sql.DataSource ds = (javax.sql.DataSource) initContext
					.lookup(jndiName);
			conn = ds.getConnection();
			return conn;
		} catch (javax.naming.NamingException nex) {
			nex.printStackTrace();
			logger.error(nex.getMessage(), nex);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public boolean login() throws LoginException {
		NameCallback nameCallback = new NameCallback("Username");
		PasswordCallback passwordCallback = new PasswordCallback("Password",
				false);
		Callback[] callbacks = new Callback[] { nameCallback, passwordCallback };
		try {

			callbackHandler.handle(callbacks);
			username = nameCallback.getName();
			char[] password = passwordCallback.getPassword();
			passwordCallback.clearPassword();

			PasswordHelper ph = new PasswordHelper();
			if (ph.authenticate(
					getSQLConnection((String) options.get(JNDINAME)), username,
					password, options)) {
				user = new UsernamePrincipal(username);
				return true;
			}

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (UnsupportedCallbackException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
			throw new LoginException(e.getMessage());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}

		return false;
	}

	@Override
	public boolean commit() throws LoginException {
		subject.getPrincipals().add(user);

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = getSQLConnection((String) options.get(JNDINAME));
		principalList = new ArrayList<Principal>();
		
		/*try {
			ps = con.prepareStatement((String) options.get(ROLESTMNT));
			ps.setString(1, username);
			rs = ps.executeQuery();
			while (rs.next()) {
				principalList.add(new InetPrincipal(rs.getString(1)));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}*/
		InetGroup group = new InetGroup("Roles");

		// for (Principal role : principalList) {
		// group.addMember(role);
		// }

		group.addMember(new InetPrincipal("AuthenticatedUser"));

		subject.getPrincipals().add(group);
		subject.getPrincipals().addAll(principalList);
		commitSucceeded = true;
		// System.out.println("Login module Commit finished");
		return true;
	}

	@Override
	public boolean abort() throws LoginException {

		if (!loginSucceeded)
			return false;
		else if (loginSucceeded && commitSucceeded) {
			loginSucceeded = false;
			user = null;
		} else
			logout();

		return true;
	}

	@Override
	public boolean logout() throws LoginException {
		subject.getPrincipals().remove(user);
		loginSucceeded = false;
		commitSucceeded = false;
		user = null;
		return true;
	}

}
