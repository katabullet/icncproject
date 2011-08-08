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
	private TblInvoice curInvoice;
	
	private ArrayList<TblInvoice> invoiceList = new ArrayList<TblInvoice>();
	Boolean visible = false;
	Boolean popupRender = false;
	
	public ArrayList<TblInvoice> getInvoiceList(){
		if(invoiceList.isEmpty()){
			invoiceList.addAll(tblInvoice.getInvoiceList());
		}
		return invoiceList;
	}	
	
	
	/*
	 * _______________________________________________________________________________________
	 */
	
	public void rowEvent(RowSelectorEvent re) {
		if(invoiceList.get(re.getRow()).isSelected()){
			invoiceList.get(re.getRow()).setSelected(true);
			setCurInvoice(invoiceList.get(re.getRow()));
			visible = true;
		}else {
			invoiceList.get(re.getRow()).setSelected(false);
			setCurInvoice(null);
			visible = false;
		}
	}

	
	/*
	 * _______________________________________________________________________________________
	 */
	
	public Boolean getVisible(){
		
		return visible;
	}


	private void setCurInvoice(TblInvoice curInvoice) {
		this.curInvoice = curInvoice;
	}


	public TblInvoice getCurInvoice() {
		return curInvoice;
	}	
	
	public Boolean getPopupRender(){
		return popupRender;
	}

	public void PopupRendernaendern(){
		popupRender = !popupRender;
		
	}
	
}
