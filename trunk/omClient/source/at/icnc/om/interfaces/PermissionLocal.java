package at.icnc.om.interfaces;

import javax.ejb.Local;

@Local
public interface PermissionLocal {
	public Integer setPermission(String username);
}
