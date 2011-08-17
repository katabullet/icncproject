package at.icnc.om.interfaces;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Local;

@Local
public interface EntityListerLocal {
	public Collection<?> getObjectList(Class<?> entityClass);
	public void DeleteObject(Long id, Class<?> entityClass);
	public void UpdateObject(Class<?> entityClass, Object updated, Long id);
	public Object getSingleObject(Class<?> entityClass);
	public Object getSingleObject(String sqlStatement, Class<?> entityClass);
	public Collection<?> getFilterList(Class<?> entityClass, String Class, String Joins, ArrayList<String> werte, ArrayList<String> spalte);
	//public Long NextID(Class<?> entityClass);
}
