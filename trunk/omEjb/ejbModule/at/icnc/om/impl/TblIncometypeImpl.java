package at.icnc.om.impl;

import java.util.ArrayList;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.entitybeans.TblOrder;
import at.icnc.om.interfaces.TblIncometypeLocal;

/**
 * Implementation for {@link TblIncometypeImpl}
 * 
 * @author csh80
 * @version 0.0.0.1
 * 
 */
@Stateful
public class TblIncometypeImpl implements TblIncometypeLocal {
	private EntityManager em;
	private EntityManagerFactory emf;

	/**
	 * Default constructor.
	 */
	public TblIncometypeImpl() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * function defined in interface TblIncometypeLocal
	 * returns list of all incometypes 
	 * (non-Javadoc)
	 * @see at.icnc.om.interfaces.TblIncometypeLocal#getIncometypeList()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<TblIncometype> getIncometypeList() {
		ArrayList<TblIncometype> result = new ArrayList<TblIncometype>();
		try {
			emf = Persistence.createEntityManagerFactory("omPU");
			em = emf.createEntityManager();
			// Query query =
			// em.createNativeQuery("SELECT a.ID_ORDER, a.ORDERNUMBER, a.ORDERDATE, a.TRAVELCOSTS, b.CUSTOMERNAME, c.DESCRIPTION_OS FROM OMORDER a left join OMCUSTOMER b on (a.FK_CUSTOMER = b.ID_CUSTOMER) left join OMORDERSTATE c on (a.FK_ORDERSTATE = c.ID_ORDERSTATE)",
			// TblOrder.class);
			String sqlStatement = "SELECT * FROM OMincometype";
			Query query = em.createNativeQuery(sqlStatement, TblIncometype.class);
			result.addAll(query.getResultList());
			em.close();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			emf.close();
		}

		return result;
	}

}
