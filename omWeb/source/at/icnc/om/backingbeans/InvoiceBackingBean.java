package at.icnc.om.backingbeans;


import java.util.ArrayList;
import javax.ejb.EJB;
import com.icesoft.faces.component.ext.RowSelectorEvent;

import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblOrder;
import at.icnc.om.entitybeans.TblSettlement;
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
	
	private TblInvoice curInvoice = new TblInvoice();
	private String filterColumn = "";
	private String filterValue = "";
	
	Boolean visible = false;
	Boolean popupRender = false;
	Boolean filterpopupRender = false;
	
	@SuppressWarnings("unchecked")
	public ArrayList<TblInvoice> getInvoiceList(){		
		ArrayList<TblInvoice> invoices = new ArrayList<TblInvoice>();
		/*invoices.addAll(tblInvoice.getInvoiceList());
		//invoices.addAll(tblInvoice.getInvoiceList("Sum","5"));
		
		if(curInvoice != null){
			for(TblInvoice curItem : invoices){				
				if(curItem.getIdInvoice() == curInvoice.getIdInvoice()){
					curItem.setSelected(true);
				}
			}
		}*/
		
		invoices.addAll((ArrayList<TblInvoice>) 
				entityLister.getObjectList("SELECT * FROM tbl_invoice", 
						TblInvoice.class));
		
		if(curInvoice != null){
			for(TblInvoice curItem : invoices){				
				if(curItem.getIdInvoice() == curInvoice.getIdInvoice()){
					curItem.setSelected(true);
					//setCurInvoice(curItem);
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
	
	public Boolean getFilterPopupRender(){
		return filterpopupRender;
	}

	public void PopupRendernaendernNew(){
		setCurInvoice(new TblInvoice());
		getCurInvoice().setTblSettlement(new TblSettlement());
		getCurInvoice().getTblSettlement().setTblOrder(new TblOrder());
		TblIncometype income = new TblIncometype();
		income.setDescriptionIt("Projekt");
		getCurInvoice().getTblSettlement().setTblIncometype(income);
		PopupRendernaendern();
	}
	
	public void PopupRendernaendern(){
		popupRender = !popupRender;		
	}
	
	public void FilterpopupRendernaendern(){
		filterpopupRender = !filterpopupRender;		
	}
	
	public void DeleteInvoice(){
		//tblInvoice.DeleteInvoice(curInvoice.getIdInvoice());
		entityLister.DeleteObject(curInvoice.getIdInvoice(), TblInvoice.class);
		visible = false;
	}
	
	public void UpdateInvoice(){
		entityLister.UpdateObject(TblInvoice.class, curInvoice);
		visible = false;
		setCurInvoice(new TblInvoice());
		PopupRendernaendern();
	}

	public void setFilterColumn(String filterColumn) {
		this.filterColumn = filterColumn;
	}

	public String getFilterColumn() {
		return filterColumn;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public String getFilterValue() {
		return filterValue;
	}
}
