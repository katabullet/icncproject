package at.icnc.om.impl;

import java.util.ArrayList;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.interfaces.TblInvoiceLocal;

/**
 * Implementation for {@link TblInvoiceLocal}
 * 
 * @author csh80
 * @version 0.0.0.1
 * 
 */
@Stateful
public class TblInvoiceImpl implements TblInvoiceLocal {

	private EntityManager em;
	private EntityManagerFactory emf;
	private static final String PU = "omPU";
    /**
     * Default constructor. 
     */
    public TblInvoiceImpl() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * @see at.icnc.om.interfaces.TblInvoiceLocal#getInvoiceList()
     * function to create a list of all invoices
     */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<TblInvoice> getInvoiceList() {
		ArrayList<TblInvoice> result = new ArrayList<TblInvoice>();
		try {		
			CreateEM(PU);
			/*emf = Persistence.createEntityManagerFactory("omPU");
			em = emf.createEntityManager();*/
		
			//Query query = em.createNativeQuery("SELECT a.ID_ORDER, a.ORDERNUMBER, a.ORDERDATE, a.TRAVELCOSTS, b.CUSTOMERNAME, c.DESCRIPTION_OS FROM OMORDER a left join OMCUSTOMER b on (a.FK_CUSTOMER = b.ID_CUSTOMER) left join OMORDERSTATE c on (a.FK_ORDERSTATE = c.ID_ORDERSTATE)", TblOrder.class);
			String sqlStatement = "SELECT * FROM OMinvoice";
			
			Query query = em.createNativeQuery(sqlStatement, TblInvoice.class);			
			
			result.addAll(query.getResultList());
			em.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally {			
			emf.close();
		}
		
		return result;
	}
	
	private void CreateEM(String persistence){
		try {
			emf = Persistence.createEntityManagerFactory(persistence);
			em = emf.createEntityManager();	
		} catch (Exception e) {
			// TODO: handle exception
		}	
	}
	
	public void DeleteInvoice(Long invoice){
		CreateEM(PU);
		TblInvoice curInvoice = em.find(TblInvoice.class, invoice);
		em.remove(curInvoice);
		em.flush();
	}
	
	public void UpdateInvoice(Long invoice){
		CreateEM(PU);
		TblInvoice curInvoice = em.find(TblInvoice.class, invoice);
		em.merge(curInvoice);
		em.flush();
	}
}
