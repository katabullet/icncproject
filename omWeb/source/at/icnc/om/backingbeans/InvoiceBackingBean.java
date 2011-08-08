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
	
	public ArrayList<TblInvoice> getInvoiceList(){
		/* invoiceList.clear();*/
		if(invoiceList.isEmpty()){
			invoiceList.addAll(tblInvoice.getInvoiceList());
		}
		//invoiceList.addAll(tblInvoice.getInvoiceList()); 
		//return tblInvoice.getInvoiceList();
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
			invoiceList.get(re.getRow()).setSelected(false);
		}else {
			invoiceList.get(re.getRow()).setSelected(true);
		}
		//tblInvoice.getInvoiceList().get(re.getRow()).setSelected(true);
	}

	
	/*
	 * _______________________________________________________________________________________
	 */
}
