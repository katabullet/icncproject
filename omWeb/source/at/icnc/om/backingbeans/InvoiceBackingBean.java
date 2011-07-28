package at.icnc.om.backingbeans;

import java.util.ArrayList;

import javax.ejb.EJB;

import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.interfaces.TblInvoiceLocal;

public class InvoiceBackingBean {
	@EJB
	TblInvoiceLocal tblInvoice;
	
	public ArrayList<TblInvoice> getOrderList(){
		ArrayList<TblInvoice> result = new ArrayList<TblInvoice>();

		result.addAll(tblInvoice.getInvoiceList());
		
		return result;
	}	
}
