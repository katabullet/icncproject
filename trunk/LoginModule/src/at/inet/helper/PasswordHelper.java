package at.inet.helper;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;

/**
 * Password based actions like creating a new password or verifying a user by
 * his password is implemented in this class. All password are hashed and a
 * random salt is added to prevent rainbow table attacks. The password are not
 * decrypted!
 *
 * Currently three hashes are supported: SHA1, SHA-384 and SHA-512
 *
 * In the year 2010 SHA1 is still considered good enough for this kind of
 * passwords. However it is advisable to use stronger hashes in the future.
 *
 * @author mme80
 *
 */
public class PasswordHelper {

	/**
	 * The name of the SelectStmnt key in the options map. The key value stores
	 * the select statement to query a user in the database. Here is a sample
	 * statement. The order in the select is important! SELECT password, salt,
	 * Pwdencyrptiontype, faliedLogins FROM MDUSER WHERE username = ?
	 */
	// Test class needs access
	public static final String SELECTSTMT = "SelectStmnt";

	/**
	 * The name of the UpdateStmnt key in the options map. The key value stores
	 * the update statement to modify the failedLogins column. Below is a sample
	 * statement. UPDATE MDUSER SET failedLogins = ? WHERE username = ?
	 */
	// Test class needs access
	public static final String UPDATESTMNT = "UpdateStmnt";

	/**
	 * The name of the IncrementFailedStmnt key in the options map. The key
	 * value stores the update statement to increment the failedLogins column.
	 * Below is a sample statement. UPDATE USERS SET FAILED_LOGINS =
	 * FAILED_LOGINS+1 WHERE ID = ?
	 */
	// Test class needs access
	public static final String INCREMENT_FAILED_STMNT = "IncrementFailedStmnt";
	/**
	 * The name of the AllowedFailedLogins key in the options map. The key value
	 * stores the number of allowed failed Logins before the account is
	 * considered locked.
	 *
	 */
	// Test class needs access
	public static final String ALLOWEDFAILEDLOGINS = "AllowedFailedLogins";

	// Test class needs access
	public static final String UNSUPPORTEDALGORITHMERROR = "Unsupported hash type found in database. Internal error.";

	private static Logger logger = Logger
			.getLogger("at.inet.helper.PasswordHelper");

	// Test class needs access
	public static final String ACCOUNTLOCKEDERROR = "The account has been locked.";

	/**
	 * Authenticates the user with a given login and password If password or
	 * login is null returns false. If the user does not exist in the database
	 * returns false.
	 *
	 * @param con
	 *            An open connection to the backend database
	 * @param login
	 *            The login of the user
	 * @param password
	 *            The password of the user
	 * @return boolean Returns true if the user is authenticated, false
	 *         otherwise
	 * @throws SQLException
	 *             If the database is inconsistent or unavailable ( (Two users
	 *             with the same login, salt or digested password altered etc.)
	 * @throws NoSuchAlgorithmException
	 *             If the used password algorithm is not supported by the JVM
	 * @throws LoginException
	 *             if the user account is locked.
	 */
	public boolean authenticate(Connection con, String login, char[] password,
			Map<String, ?> options) throws SQLException,
			NoSuchAlgorithmException, LoginException {

		PreparedStatement ps = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;
		int failedLogins = 0;
		int allowedFailedLogins = 0;
		if (!options.containsKey(ALLOWEDFAILEDLOGINS)) {
			logger.warn("The key ALLOWEDFAILEDLOGINS cound not the found in the passed MAP.");
			logger.warn("Assuming 0 allowed failedLogins. You may not be happy this assumption so fix your setup!");
		} else
			allowedFailedLogins = Integer.parseInt(((String) options
					.get(ALLOWEDFAILEDLOGINS)));
		try {
			boolean userValid = true;
			// INPUT VALIDATION
			if (login == null || password == null) {
				// TIME RESISTANT ATTACK
				// Computation time is equal to the time needed by a legitimate
				// user
				userValid = false;
				login = "";
				password = new char[0];
			}
			ps = con.prepareStatement((String) options.get(SELECTSTMT));

			ps.setString(1, login);
			ps.setString(2, new String(password));
			rs = ps.executeQuery();
			if (rs.next()) {
				failedLogins = rs.getInt(1);
				if ((failedLogins > allowedFailedLogins)
						&& (allowedFailedLogins >= 0))
					throw new LoginException(ACCOUNTLOCKEDERROR);
				if (rs.next()) { // Should not append, because login is the
									// primary key
					throw new SQLException(
							"Database inconsistent two CREDENTIALS with the same LOGIN");
				}
			} else {
				// TIME RESISTANT ATTACK (Even if the user does not exist
				// the Computation time is equal to the time needed for a legitimate user
				userValid = false;
			}

			if (!userValid) {
				// if the account lock feature has been disabled don't update
				// the counter.
				if (allowedFailedLogins >= 0) {
					psUpdate = con.prepareStatement((String) options
							.get(INCREMENT_FAILED_STMNT));
					psUpdate.setString(1, login);
					psUpdate.executeUpdate();
				}
				psUpdate = con.prepareStatement((String) options
						.get(UPDATESTMNT));
				psUpdate.setString(2, login);
				psUpdate.setInt(1, 0);
				psUpdate.executeUpdate();
				return false;
			}
			return true;
		} finally {
			close(rs);
			close(psUpdate);
			close(ps);
			con.close();

		}
	}

	/**
	 * Closes the current statement
	 *
	 * @param ps
	 *            Statement
	 */
	public void close(Statement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException ignore) {
			}
		}
	}

	/**
	 * Closes the current resultset
	 *
	 * @param ps
	 *            Statement
	 */
	public void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ignore) {
			}
		}
	}

	/**
	 * Helper method. Wraps the passed string in ' quotes. Handy helper to
	 * generate a valid Insert statement manually.
	 *
	 * @param data
	 *            the string to wrap
	 * @return the input string in format 'input string'
	 */
	public String encapsulate(String data) {
		StringBuffer sb = new StringBuffer("'");
		sb.append(data).append("'");
		return sb.toString();
	}
}
