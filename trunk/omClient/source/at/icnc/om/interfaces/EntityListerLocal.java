package at.icnc.om.interfaces;

import java.util.Collection;

import javax.ejb.Local;

@Local
public interface EntityListerLocal {
	public Collection<?> getObjectList(String sqlStatement, Class<?> entityClass);
}