package inet.module.mdm.security;

import java.security.Permission;

/**
 * {@link AdministrationPermission} protects the following administrative
 * actions:
 * <ul>
 * <li>Define MDM users (name=DEFINE_USERS)</li>
 * <li>Define MDM roles (name=DEFINE_ROLES)</li>
 * <li>Define permission ids (name=DEFINE_PERMISSION_IDS)</li>
 * <li>Edit help texts (name=EDIT_HELP_TEXTS)</li>
 * <li>Define validation rules (name=DEFINE_VALIDATION_RULES)</li>
 * </ul>
 * This permission has no action-string and no wildcards are allowed!
 *
 * @author fre80
 * @version $Id: AdministrationPermission.java 3188 2011-01-13 14:31:33Z aku80 $
 *
 */
public class AdministrationPermission extends Permission {
	private static final long serialVersionUID = 1L;

	public AdministrationPermission(String name) {
		super(name);
		if (name == null) {
			throw new IllegalArgumentException(
					"name is not allowed to be null!");
		}
	}

	@Override
	public boolean implies(Permission permission) {
		if (!(permission instanceof AdministrationPermission)) {
			return false;
		}
		return getName().equals(permission.getName());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AdministrationPermission)) {
			return false;
		}
		return getName().equals(((AdministrationPermission) obj).getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public String getActions() {
		return null;
	}

}
