package at.icnc.om.impl;

import java.util.ArrayList;

import javax.ejb.Stateful;
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
@Stateful
public class TblOrderImpl implements TblOrderLocal{

	private EntityManager em;
	private EntityManagerFactory emf;
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
			emf = Persistence.createEntityManagerFactory("omPU");
			em = emf.createEntityManager();
			//Query query = em.createNativeQuery("SELECT a.ID_ORDER, a.ORDERNUMBER, a.ORDERDATE, a.TRAVELCOSTS, b.CUSTOMERNAME, c.DESCRIPTION_OS FROM OMORDER a left join OMCUSTOMER b on (a.FK_CUSTOMER = b.ID_CUSTOMER) left join OMORDERSTATE c on (a.FK_ORDERSTATE = c.ID_ORDERSTATE)", TblOrder.class);
			String sqlStatement = "SELECT * FROM OMorder";
			Query query = em.createNativeQuery(sqlStatement, TblOrder.class);
			result.addAll(query.getResultList());			
			em.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{			
			emf.close();
		}
		
		return result;
	}

}
