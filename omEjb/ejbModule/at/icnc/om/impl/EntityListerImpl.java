package at.icnc.om.impl;

import java.util.ArrayList;
import java.util.Collection;

import at.icnc.om.interfaces.EntityListerLocal;
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
			emf = Persistence.createEntityManagerFactory("omPU");
			em = emf.createEntityManager();
			Query query = em.createNativeQuery(sqlStatement, entityClass.getSimpleName());
			result.addAll(query.getResultList());
			em.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally {			
			emf.close();
		}
		
		return result;
	}
}
