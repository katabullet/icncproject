package at.inet.jaas.login;

import java.io.Serializable;
import java.security.Principal;

public class InetPrincipal implements Principal, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8350961329759985846L;

	private final String name;

	public InetPrincipal (String name) {
		this.name = name;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InetPrincipal other = (InetPrincipal) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	@Override
	public String getName() {
 		return name;
	}

}
