package at.icnc.om.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import at.icnc.om.interfaces.EntityListerLocal;

import javax.annotation.PreDestroy;
import javax.ejb.Local;
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
	
	private Query CreateQuery(String sqlStatement, Class<?> entityClass, EntityManager curEM){
		Query query = em.createNativeQuery(sqlStatement, entityClass);	
		return query;
	}
	
	private Query CreateQuery(String sqlStatement, EntityManager curEM){
		Query query = curEM.createNativeQuery(sqlStatement);
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
	
	public Long NextID(Class<?> entityClass){		
		String classname = entityClass.getSimpleName();
		
		classname = classname.substring(3);
		String sqlStatement = "SELECT OM" + classname + "_seq.nextval FROM dual";
		
		//return ((BigDecimal) CreateQuery("SELECT " + classname + "_SEQ.Nextval FROM DUAL", entityClass, CreateEM(PU)).getResultList().get(0)).longValue();
		return ((BigDecimal)CreateQuery(sqlStatement, CreateEM(PU)).getSingleResult()).longValue();
	}
	
	public void UpdateObject(Class<?> entityClass, Object updated){
		
		try {
			CreateEM(PU);
			
			em.merge(updated);
			
			
			//em.persist(updated);
			
			em.flush();
			em.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	
	
}
