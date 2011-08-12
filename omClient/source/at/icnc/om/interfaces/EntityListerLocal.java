package at.icnc.om.interfaces;

import java.util.Collection;

import javax.ejb.Local;

@Local
public interface EntityListerLocal {
	public Collection<?> getObjectList(String sqlStatement, Class<?> entityClass);
	public void DeleteObject(Long id, Class<?> entityClass);
	public void UpdateObject(Class<?> entityClass, Object updated, Long id);
	public Object getSingleObject(String sqlStatement, Class<?> entityClass);
	//public Long NextID(Class<?> entityClass);
}
