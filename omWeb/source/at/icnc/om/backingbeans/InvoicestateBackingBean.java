package at.icnc.om.backingbeans;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import at.icnc.om.entitybeans.TblInvoicestate;
import at.icnc.om.interfaces.TblInvoicestateLocal;

public class InvoicestateBackingBean {
	
		@EJB
		private TblInvoicestateLocal tblInvoicestate;
		
		ArrayList<TblInvoicestate> invoicestates = new ArrayList<TblInvoicestate>();
		
		public ArrayList<TblInvoicestate> getInvoicestateList(){
			invoicestates.clear();
			invoicestates.addAll(tblInvoicestate.getInvoicsestateList());
			return invoicestates;
		}	
		
		public ArrayList<SelectItem> getInvoicestateListDescription(){
			ArrayList<SelectItem> invoicsestates = new ArrayList<SelectItem>();
			for (TblInvoicestate item : tblInvoicestate.getInvoicsestateList()) {
				invoicsestates.add(new SelectItem(item.getDescriptionIs()));
			}
			return invoicsestates;
		}
}
