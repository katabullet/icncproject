package at.icnc.om.impl;

import java.util.ArrayList;
import java.util.Collection;

import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.interfaces.EntityListerLocal;

import javax.annotation.PreDestroy;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Session Bean implementation class EntityListerImpl
 */
@Stateless
@Local( { EntityListerLocal.class })
public class EntityListerImpl implements EntityListerLocal {
	
	private EntityManager em;
	private EntityManagerFactory emf;
	private static final String PU = "omPU";

    /**
     * Default constructor. 
     */
    public EntityListerImpl() {
        // TODO Auto-generated constructor stub
    }

	@SuppressWarnings("unchecked")
	@Override
	public Collection<?> getObjectList(String sqlStatement, Class<?> entityClass) {
		Collection<?> result = new ArrayList<Object>();
		
		try {		
			//CreateEM(PU);
			result.addAll(CreateQuery(sqlStatement, entityClass, CreateEM(PU)).getResultList());
			em.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally {			
			emf.close();
			//em.close();
		}		
		
		return result;
	}
	
	private Query CreateQuery(String sqlStatement, Class<?> entityClass, EntityManager curEM){
		Query query = em.createNativeQuery(sqlStatement, entityClass);	
		return query;
	}
	
	private EntityManager CreateEM(String persistence){
		try {
			emf = Persistence.createEntityManagerFactory(persistence);
			em = emf.createEntityManager();	
		} catch (Exception e) {
			// TODO: handle exception
		}	
		return em;
	}
	
	public void DeleteObject(Long id, Class<?> entityClass){
		
		try {
			CreateEM(PU);
			Object curObject = entityClass.cast(em.find(entityClass, id));
			em.remove(curObject);
			em.flush();
			em.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			emf.close();
		}
	}
	
	public void UpdateObject(Class<?> entityClass, Object updated){
		
		try {
			CreateEM(PU);
			em.merge(updated);
			em.flush();
			em.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			emf.close();
		}
	}	
	
	@SuppressWarnings("unused")
	private String StatementCreater(String table, String[] filterColumn, 
									String[] filterValue){
		String sqlStatement = "SELECT * FROM " + table;
		if(filterColumn != null && filterValue != null){
			sqlStatement += " WHERE ";
			for(int x = 0; x< filterColumn.length; x++){
				sqlStatement += " AND ";
				sqlStatement += filterColumn[x] + " LIKE '%" + filterValue[x] + "%'";
			}
		}
		sqlStatement = sqlStatement.substring(0, sqlStatement.length() - 4);
		return sqlStatement;
	}
	
	@PreDestroy
	public void BeforeDestroy(){
		em.close();
		emf.close();
	}
	
	
}
