package at.icnc.om.impl;

import java.util.ArrayList;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import at.icnc.om.entitybeans.TblInvoicestate;
import at.icnc.om.interfaces.TblInvoicestateLocal;

/**
 * Implementation for {@link TblInvoicestateImpl}
 * 
 * @author nkn80
 * @version 0.0.0.1
 * 
 */
@Stateful
public class TblInvoicestateImpl implements TblInvoicestateLocal {
	private EntityManager em;
	private EntityManagerFactory emf;

	/**
	 * Default constructor.
	 */
	public TblInvoicestateImpl() {
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
	public ArrayList<TblInvoicestate> getInvoicsestateList() {
		ArrayList<TblInvoicestate> result = new ArrayList<TblInvoicestate>();
		try {
			emf = Persistence.createEntityManagerFactory("omPU");
			em = emf.createEntityManager();
			// Query query =
			// em.createNativeQuery("SELECT a.ID_ORDER, a.ORDERNUMBER, a.ORDERDATE, a.TRAVELCOSTS, b.CUSTOMERNAME, c.DESCRIPTION_OS FROM TBL_ORDER a left join TBL_CUSTOMER b on (a.FK_CUSTOMER = b.ID_CUSTOMER) left join TBL_ORDERSTATE c on (a.FK_ORDERSTATE = c.ID_ORDERSTATE)",
			// TblOrder.class);
			String sqlStatement = "SELECT * FROM tbl_invoicestate";
			Query query = em.createNativeQuery(sqlStatement, TblInvoicestate.class);
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
