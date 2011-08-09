package at.icnc.om.backingbeans;


import java.util.ArrayList;
import javax.ejb.EJB;
import com.icesoft.faces.component.ext.RowSelectorEvent;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.interfaces.TblIncometypeLocal;
import at.icnc.om.interfaces.TblInvoiceLocal;

public class InvoiceBackingBean {
	@EJB
	TblInvoiceLocal tblInvoice;
	@EJB
	TblIncometypeLocal tblIncometype;
	private TblInvoice curInvoice = new TblInvoice();
	
	Boolean visible = false;
	Boolean popupRender = false;
	
	public ArrayList<TblInvoice> getInvoiceList(){		
		ArrayList<TblInvoice> invoices = new ArrayList<TblInvoice>();
		invoices.addAll(tblInvoice.getInvoiceList());
		
		if(curInvoice != null){
			for(TblInvoice curItem : invoices){				
				if(curItem.getIdInvoice() == curInvoice.getIdInvoice()){
					curItem.setSelected(true);
				}
			}
		}

		return invoices;
	}	
	
	
	/*
	 * _______________________________________________________________________________________
	 */
	
	public void rowEvent(RowSelectorEvent re) {
		
		if(getCurInvoice() != null){
			
			if(getCurInvoice().getIdInvoice() == tblInvoice.getInvoiceList().get(re.getRow()).getIdInvoice()){
				setCurInvoice(new TblInvoice());
			}else {
				setCurInvoice(tblInvoice.getInvoiceList().get(re.getRow()));
			}
		}
	}

	
	/*
	 * _______________________________________________________________________________________
	 */
	
	public Boolean getVisible(){
		
		return visible;
	}


	public void setCurInvoice(TblInvoice curInvoice) {
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
