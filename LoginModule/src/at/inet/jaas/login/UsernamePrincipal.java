package at.inet.jaas.login;

import java.security.Principal;

/**
 * Principal holding the username of the current subject.
 * 
 * @author fre80
 * @version $Id: UsernamePrincipal.java 4071 2011-03-03 14:09:00Z fre80 $
 * 
 */
public class UsernamePrincipal implements Principal {

	private String username;

	public UsernamePrincipal(String username) {
		this.username = username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName() {
		return username;
	}

}
