package at.icnc.om.impl;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import at.icnc.om.entitybeans.TblOrder;
import at.icnc.om.interfaces.TblOrderLocal;

/**
 * Implementation for {@link TblOrderImpl}
 * 
 * @author csh80
 * @version 0.0.0.1
 * 
 */
@Stateless
public class TblOrderImpl implements TblOrderLocal{

	private EntityManager em;
    /**
     * Default constructor. 
     */
    public TblOrderImpl() {
        // TODO Auto-generated constructor stub
    }

    /* 
     * This function was defined in the TblOrderLocal-Interface
     * New functions or methods must also be defined over the interface
     * @see at.icnc.om.interfaces.TblOrderLocal#getOrderList()
     * 
     */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<TblOrder> getOrderList() {
		ArrayList<TblOrder> result = new ArrayList<TblOrder>();
		try {		
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("omPU");
			em = emf.createEntityManager();
			Query query = em.createNativeQuery("SELECT ID_ORDER, ORDERNUMBER, TRAVELCOSTS, ORDERDATE FROM TBL_ORDER", TblOrder.class);
			result.addAll(query.getResultList());
		} catch (Exception e) {
			// TODO: handle exception
		}		
		
		return result;
	}

}
