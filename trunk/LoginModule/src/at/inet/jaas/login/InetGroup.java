package at.inet.jaas.login;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class InetGroup implements Group, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 159430654907345194L;


	private final String name;

	private final Set<Principal> users = new HashSet<Principal>();


	public InetGroup(String name) {
		this.name = name;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public boolean addMember(Principal user) {
		return users.add(user);
	}

	@Override
	public boolean removeMember(Principal user) {
		return users.remove(user);
	}

	@Override
	public boolean isMember(Principal member) {
		return users.contains(member);
	}

	@Override
	public Enumeration<? extends Principal> members() {
		return Collections.enumeration(users);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
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
		InetGroup other = (InetGroup) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}



}
