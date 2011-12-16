package at.icnc.om.impl;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PreDestroy;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import at.icnc.om.interfaces.EntityListerLocal;

/**
 * Session Bean implementation class EntityListerImpl
 * @author csh80
 */
@Stateless
@Local( { EntityListerLocal.class })
public class EntityListerImpl implements EntityListerLocal {
	
	private EntityManager em;
	private EntityManagerFactory emf;
	/* PersistenceUnit */
	private static final String PU = "omPU"; 

    /**
     * Default constructor. 
     * nothing is done on creation
     */
    public EntityListerImpl() {
    }
    
    /**
     * Returns a list of all entities of the defined entityClass
     * @param entityClass class of specific entity (entityname.class)
     */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<?> getObjectList(Class<?> entityClass) {
		/* local list for all results */
		Collection<?> result = new ArrayList<Object>();
		
		/* Creates the SQL-Statement using QueryBuilder 
		 * sqlStatement without any filters 
		 */
		String sqlStatement = QueryBuilder.CreateSelectStatement(entityClass);
		
		try {		
			/* Uses create Query to create a Query
			 * Query-Method getResultList is used
			 */
			result.addAll(CreateQuery(sqlStatement, entityClass, CreateEM(PU)).getResultList());
			em.close();
		} catch (Exception e) {
			
		}finally {			
			emf.close();
		}	
		
		return result;
	}
	
	 /**
     * Returns a list of all entities of the defined entityClass
     * @param entityClass class of specific entity (entityname.class)
     */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<?> getObjectListSalesman(Class<?> entityClass, String Class, String username, String join) {
		/* local list for all results */
		Collection<?> result = new ArrayList<Object>();
		
		/* Creates the SQL-Statement using QueryBuilder 
		 * sqlStatement without any filters 
		 */
		String sqlStatement = QueryBuilder.CreateSelectStatement(Class, username, join);
		
		try {		
			/* Uses create Query to create a Query
			 * Query-Method getResultList is used
			 */
			//result.addAll(CreateQuery(sqlStatement, entityClass, CreateEM(PU)).getResultList());
			
			Query query = CreateEM(PU).createQuery(sqlStatement);
			result.addAll(query.getResultList());
			em.close();
		} catch (Exception e) {
			
		}finally {			
			emf.close();
		}	
		
		return result;
	}
	
	public Object getSingleObject(Class<?> entityClass){
		Object result = new Object();
		String sqlStatement = QueryBuilder.CreateSelectStatement(entityClass);
		
		try {
			result = CreateQuery(sqlStatement, entityClass, CreateEM(PU)).getSingleResult();
			em.close();
		} catch (Exception e) {
			emf.close();
		}
		
		return result;
	}
	
	public Object getSingleObject(String sqlStatement, Class<?> entityClass){
		Object result = new Object();
		
		try {
			result = CreateQuery(sqlStatement, entityClass, CreateEM(PU)).getSingleResult();
			em.close();
		} catch (Exception e) {
			// TODO: handle exception
			emf.close();
		}
		
		return result;
	}
	
	/**
	 * Creates a Query that can be used to get a single result or resultlist
	 * 
	 * @param sqlStatement 
	 * @param entityClass 
	 * @param curEM 
	 * @return the created query is returned
	 */
	private Query CreateQuery(String sqlStatement, Class<?> entityClass, EntityManager curEM){
		Query query = em.createNativeQuery(sqlStatement, entityClass);	
		return query;
	}	
	
	/**
	 * Creates an entityManager using an entityManagerFactory 
	 * 
	 * @param persistence Name of the persistence Unit
	 * @return the created entityManager is returned
	 */
	private EntityManager CreateEM(String persistence){
		try {
			emf = Persistence.createEntityManagerFactory(persistence);
			em = emf.createEntityManager();	
		} catch (Exception e) {
		}	
		return em;
	}
	
	/**
	 * An entity with the specified id of type entityClass is deleted
	 * @param id id of the selected entity
	 * @param entityClass Classtype of the selected entity (entityname.class)
	 */
	public void DeleteObject(Long id, Class<?> entityClass){
		
		try {
			CreateEM(PU);
			Object curObject = entityClass.cast(em.find(entityClass, id));
			em.remove(curObject);
			em.flush();
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			emf.close();
		}
	}	
	
	/**
	 * An entity with the specified id of type entityClass is updated or added
	 * @param id id of the selected entity
	 * @param entityClass Classtype of the selected entity (entityname.class)
	 * @param updated the object that will be added or updated
	 */
	public void UpdateObject(Class<?> entityClass, Object updated, Long id){
		
		try {
			CreateEM(PU);
			
			if(em.find(entityClass, id) != null){
				em.merge(updated);
			}else{
				em.persist(updated);
			}	
			
			em.flush();
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			emf.close();
		}
	}	
	
	@PreDestroy
	public void BeforeDestroy(){
		em.close();
		emf.close();
	}

	@SuppressWarnings("unchecked")
	public Collection<?> getFilterList(Class<?> entityClass, String Class, String Joins, ArrayList<String> werte, ArrayList<String> spalte) {
		/* local list for all results */
		Collection<?> result = new ArrayList<String>();
		
		/* Creates the SQL-Statement using QueryBuilder 
		 * sqlStatement without any filters 
		 */
		String sqlStatement = QueryBuilder.CreateSelectStatement(Class, Joins, spalte, werte);
		
		try {		
			/* Uses create Query to create a Query
			 * Query-Method getResultList is used
			 */
			//result.addAll(CreateQuery(sqlStatement, entityClass, CreateEM(PU)).getResultList());
			
			Query query = CreateEM(PU).createQuery(sqlStatement);
			result.addAll(query.getResultList());
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			emf.close();
		}	
		
		return result;
	}
	
	/**
     * 
     */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<?> getObjectList(String sqlStatement) {
		/* local list for all results */
		Collection<?> result = new ArrayList<Object>();
		
		try {		
			/* Uses create Query to create a Query
			 * Query-Method getResultList is used
			 */
			Query query = em.createQuery(sqlStatement);
			result.addAll(query.getResultList());
			em.close();
		} catch (Exception e) {
			
		}finally {			
			emf.close();
		}	
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<?> getReminderList(ArrayList<String> werte, ArrayList<String> spalte) {
		/* local list for all results */
		Collection<?> result = new ArrayList<String>();
		
		/* Creates the SQL-Statement using QueryBuilder 
		 * sqlStatement without any filters 
		 */
		String sqlStatement = QueryBuilder.CreateReminderStatement(spalte, werte);
		
		try {		
			/* Uses create Query to create a Query
			 * Query-Method getResultList is used
			 */
			//result.addAll(CreateQuery(sqlStatement, entityClass, CreateEM(PU)).getResultList());
			
			Query query = CreateEM(PU).createQuery(sqlStatement);
			result.addAll(query.getResultList());
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			emf.close();
		}	
		
		return result;
	}
}
