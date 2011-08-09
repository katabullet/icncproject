package at.icnc.om.backingbeans;


import java.util.ArrayList;
import javax.ejb.EJB;
import com.icesoft.faces.component.ext.RowSelectorEvent;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblOrder;
import at.icnc.om.entitybeans.TblSettlement;
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

	public void rowEvent(RowSelectorEvent re) {
		
		if(getCurInvoice() != null){
			
			if(getCurInvoice().getIdInvoice() == tblInvoice.getInvoiceList().get(re.getRow()).getIdInvoice()){
				setCurInvoice(new TblInvoice());
				visible = false;
			}else {
				setCurInvoice(tblInvoice.getInvoiceList().get(re.getRow()));
				visible = true;
			}
		}
	}
	
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

	public void PopupRendernaendernNew(){
		setCurInvoice(new TblInvoice());
		getCurInvoice().setTblSettlement(new TblSettlement());
		getCurInvoice().getTblSettlement().setTblOrder(new TblOrder());
		PopupRendernaendern();
	}
	
	public void PopupRendernaendern(){
		popupRender = !popupRender;
		
	}
	
	public void DeleteInvoice(){
		tblInvoice.DeleteInvoice(curInvoice.getIdInvoice());
		visible = false;
	}
}
