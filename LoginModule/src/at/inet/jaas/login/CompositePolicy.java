package at.inet.jaas.login;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Implentation of the composite pattern for {@link Policy}s.
 * 
 * @author fre80
 * @version $Id: CompositePolicy.java 4071 2011-03-03 14:09:00Z fre80 $
 * 
 */
public class CompositePolicy extends Policy {
	private List<Policy> policies = Collections.emptyList();

	public CompositePolicy(List<Policy> policies) {
		this.policies = policies;
	}

	@Override
	public boolean implies(ProtectionDomain domain, Permission permission) {
		for (Policy policy : policies) {
			if (policy.implies(domain, permission)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public PermissionCollection getPermissions(ProtectionDomain domain) {
		Permissions permissions = new Permissions();
		for (Policy policy : policies) {
			for (Enumeration<Permission> permissionElements = policy
					.getPermissions(domain).elements(); permissionElements
					.hasMoreElements();) {
				permissions.add(permissionElements.nextElement());
			}
		}
		permissions.setReadOnly();
		return permissions;
	}

	@Override
	public PermissionCollection getPermissions(CodeSource codesource) {
		Permissions permissions = new Permissions();
		for (Policy policy : policies) {
			for (Enumeration<Permission> permissionEnumeration = policy
					.getPermissions(codesource).elements(); permissionEnumeration
					.hasMoreElements();) {
				permissions.add(permissionEnumeration.nextElement());
			}
		}
		permissions.setReadOnly();
		return permissions;
	}

	@Override
	public void refresh() {
		for (Policy policy : policies) {
			policy.refresh();
		}
	}
}
