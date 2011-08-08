package at.icnc.om.backingbeans;


import java.util.ArrayList;
import javax.ejb.EJB;
import com.icesoft.faces.component.ext.RowSelectorEvent;

import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.interfaces.TblIncometypeLocal;
import at.icnc.om.interfaces.TblInvoiceLocal;

public class InvoiceBackingBean {
	@EJB
	TblInvoiceLocal tblInvoice;
	@EJB
	TblIncometypeLocal tblIncometype;

	
	private ArrayList<TblInvoice> invoiceList = new ArrayList<TblInvoice>();
	Boolean visible = false;
	
	public ArrayList<TblInvoice> getInvoiceList(){
		if(invoiceList.isEmpty()){
			invoiceList.addAll(tblInvoice.getInvoiceList());
		}
		return invoiceList;
	}	
	
	public ArrayList<TblIncometype> getIncometypeList(){
		ArrayList<TblIncometype> result = new ArrayList<TblIncometype>();
		
		result.addAll(tblIncometype.getIncometypeList());
		
		return result;
	}
	/*
	 * _______________________________________________________________________________________
	 */
	
	public void rowEvent(RowSelectorEvent re) {
		if(invoiceList.get(re.getRow()).isSelected()){
			invoiceList.get(re.getRow()).setSelected(true);
			visible = true;
		}else {
			invoiceList.get(re.getRow()).setSelected(false);
			visible = false;
		}
	}

	
	/*
	 * _______________________________________________________________________________________
	 */
	
	public Boolean getVisible(){
		
		return visible;
	}	
	
}
