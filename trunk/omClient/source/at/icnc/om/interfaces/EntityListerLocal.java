package at.icnc.om.interfaces;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Local;

/**
 * Local Interface for EntityListerImpl
 * @author csh80
 *
 */
@Local
public interface EntityListerLocal {
	
	/* Function to return entityList without filters */
	public Collection<?> getObjectList(Class<?> entityClass);
	
	/* Function to return entityList */
	public Collection<?> getObjectList(String sqlStatement);
	
	/* Method to delete a specific entity */
	public void DeleteObject(Long id, Class<?> entityClass);
	
	/* Method to update a specific entity */
	public void UpdateObject(Class<?> entityClass, Object updated, Long id);
	
	/* Function to get a single entity */
	public Object getSingleObject(Class<?> entityClass);
	
	/* Function to get a single entity including a self-made statement */
	public Object getSingleObject(String sqlStatement, Class<?> entityClass);

	/* Function to get a filtered entity-list */
	public Collection<?> getFilterList(Class<?> entityClass, String Class, String Joins, ArrayList<String> werte, ArrayList<String> spalte);
	
	/* Function to get a filtered entity-list */
	public Collection<?> getReminderList(ArrayList<String> werte, ArrayList<String> spalte);

}
