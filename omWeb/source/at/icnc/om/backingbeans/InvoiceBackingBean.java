package at.icnc.om.backingbeans;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;

import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.interfaces.EntityListerLocal;
import at.icnc.om.interfaces.TblIncometypeLocal;
import at.icnc.om.interfaces.TblInvoiceLocal;

public class InvoiceBackingBean {
	@EJB
	TblInvoiceLocal tblInvoice;
	@EJB
	TblIncometypeLocal tblIncometype;
	@EJB
	EntityListerLocal entityLister;
	
	public ArrayList<TblInvoice> getOrderList(){
		ArrayList<TblInvoice> result = new ArrayList<TblInvoice>();

		result.addAll(tblInvoice.getInvoiceList());
		
		return result;
	}	
	
	public ArrayList<TblIncometype> getIncometypeList(){
		ArrayList<TblIncometype> result = new ArrayList<TblIncometype>();
		
		result.addAll(tblIncometype.getIncometypeList());
		
		return result;
	}
	
	/* @SuppressWarnings("unchecked")
	public ArrayList<TblIncometype> getEntityList(){
		ArrayList<TblIncometype> result = new ArrayList<TblIncometype>();
		
		result.addAll((Collection<? extends TblIncometype>) entityLister.getObjectList("SELECT * FROM TBL_INCOMETYPE"));
		
		return result;
	}*/
}
