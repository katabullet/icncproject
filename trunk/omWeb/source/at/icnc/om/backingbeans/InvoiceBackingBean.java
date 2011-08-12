package at.icnc.om.backingbeans;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.datapaginator.DataPaginator;
import com.icesoft.faces.component.ext.RowSelectorEvent;

import antlr.debug.NewLineEvent;
import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblInvoicestate;
import at.icnc.om.entitybeans.TblOrder;
import at.icnc.om.entitybeans.TblSettlement;
import at.icnc.om.interfaces.EntityListerLocal;
import at.icnc.om.interfaces.TblIncometypeLocal;
import at.icnc.om.interfaces.TblInvoiceLocal;
import at.icnc.om.interfaces.TblInvoicestateLocal;
import at.icnc.om.interfaces.TblOrderLocal;

public class InvoiceBackingBean {
	@EJB
	TblInvoiceLocal tblInvoice;
	@EJB
	TblIncometypeLocal tblIncometype;
	@EJB
	EntityListerLocal entityLister;
	@EJB
	TblOrderLocal tblOrder;
	@EJB
	TblInvoicestateLocal tblInvoicestate;
	
	private TblInvoice curInvoice = new TblInvoice();
	private String filterColumn = "";
	private String filterValue = "";
	
	Boolean visible = false;
	Boolean popupRender = false;
	Boolean filterpopupRender = false;
	private ArrayList<TblInvoice> invoiceList;
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<TblInvoice> getInvoiceList(){		
		//ArrayList<TblInvoice> invoices = new ArrayList<TblInvoice>();
		/*invoices.addAll(tblInvoice.getInvoiceList());
		//invoices.addAll(tblInvoice.getInvoiceList("Sum","5"));
		*/
		if(invoiceList == null){
			invoiceList = new ArrayList<TblInvoice>();
			invoiceList.addAll((ArrayList<TblInvoice>) 
					entityLister.getObjectList("SELECT * FROM OMinvoice", 
							TblInvoice.class));
		}
		
		if(curInvoice != null){
			for(TblInvoice curItem : invoiceList){				
				if(curItem.getIdInvoice() == curInvoice.getIdInvoice()){
					curItem.setSelected(true);
				}
			}
		}
		/* invoices.addAll((ArrayList<TblInvoice>) 
				entityLister.getObjectList("SELECT * FROM OMinvoice", 
						TblInvoice.class));
		
		if(curInvoice != null){
			for(TblInvoice curItem : invoices){				
				if(curItem.getIdInvoice() == curInvoice.getIdInvoice()){
					curItem.setSelected(true);
					//setCurInvoice(curItem);
				}
			}
		}*/
		
		//return invoices;
		return invoiceList;
	}	

	public void rowEvent(RowSelectorEvent re) {		
		/*if(getCurInvoice() != null){			
			if(getCurInvoice().getIdInvoice() == tblInvoice.getInvoiceList().get(re.getRow()).getIdInvoice()){
				setCurInvoice(new TblInvoice());
				visible = false;
			}else {
				setCurInvoice(tblInvoice.getInvoiceList().get(re.getRow()));
				visible = true;
			}
		}*/
		
		if(getCurInvoice() != null){			
			if(getCurInvoice().getIdInvoice() == invoiceList.get(re.getRow()).getIdInvoice()){
				setCurInvoice(new TblInvoice());
				visible = false;
			}else {
				setCurInvoice(invoiceList.get(re.getRow()));
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
		setCurFilterInvoice(new TblInvoice());
		getCurFilterInvoice().setIdInvoice(0);
		
		return filterpopupRender;
	}

	public void PopupRendernaendernNew(){
		/* setCurInvoice(new TblInvoice());

		//getCurInvoice().setIdInvoice(entityLister.NextID(TblInvoice.class));
		getCurInvoice().setDuedate(new Date());
		getCurInvoice().setTblSettlement((TblSettlement) entityLister.getSingleObject("SELECT * FROM OMSETTLEMENT WHERE id_settlement = 1", TblSettlement.class));
		getCurInvoice().getTblSettlement().setTblOrder((TblOrder) entityLister.getSingleObject("SELECT * FROM OMorder WHERE id_order = 1", TblOrder.class));
		getCurInvoice().setTblInvoicestate((TblInvoicestate) entityLister.getSingleObject("SELECT * FROM OMinvoicestate WHERE id_invoicestate = 1", TblInvoicestate.class));
		
		getCurInvoice().getTblSettlement().setTblIncometype((TblIncometype) entityLister.getSingleObject("SELECT * FROM OMincometype WHERE id_incometype = 2", TblIncometype.class));

		getCurInvoice().setTblSettlement(new TblSettlement());
		TblIncometype income = new TblIncometype();
		List<TblIncometype> defaultWertIncometype = (List<TblIncometype>)entityLister.getObjectList("SELECT  * FROM OMINCOMETYPE", TblIncometype.class);
		income.setDescriptionIt(defaultWertIncometype.get(0).getDescriptionIt());
		getCurInvoice().getTblSettlement().setTblIncometype(income);
		//Defaultwert für Auftragsnummer setzen
		TblOrder order = new TblOrder();
		List<TblOrder> defaultListeOrder =(List<TblOrder>) entityLister.getObjectList("SELECT  * FROM OMorder", TblOrder.class);
		TblOrder defaultWertOrder = defaultListeOrder.get(defaultListeOrder.size()-1);
		order.setOrdernumber(defaultWertOrder.getOrdernumber());
		getCurInvoice().getTblSettlement().setTblOrder(order);
		//Defaultwert für Rechnungstatus setzen
		TblInvoicestate invoicestate = new TblInvoicestate();
		List<TblInvoicestate> defaultWertInvoicestate = (List<TblInvoicestate>)entityLister.getObjectList("SELECT  * FROM OMinvoicestate", TblInvoicestate.class);
		invoicestate.setDescriptionIs(defaultWertInvoicestate.get(0).getDescriptionIs());
		getCurInvoice().setTblInvoicestate(invoicestate);*/
		
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
		invoiceList = null;
		visible = false;
	}
	
	public void UpdateInvoice(){
		curInvoice.setTblInvoicestate(curInvoicestate);
		entityLister.UpdateObject(TblInvoice.class, curInvoice);
		visible = false;
		invoiceList = null;
		invoicestates = null;
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
	
	// __________________________________________________________________________
	
	
	ArrayList<TblInvoicestate> invoicestates;
	private TblInvoicestate curInvoicestate;
	
	@SuppressWarnings("unchecked")
	private ArrayList<TblInvoicestate> getInvoicestateList(){
		/*invoicestates.clear();
		invoicestates.addAll(tblInvoicestate.getInvoicsestateList());
		return invoicestates;*/
		if(invoicestates == null){
			invoicestates = new ArrayList<TblInvoicestate>();
			invoicestates.addAll((Collection<? extends TblInvoicestate>) 
					entityLister.getObjectList("SELECT * FROM OMinvoicestate", 
							TblInvoicestate.class));
		}
		
		if(curInvoicestate != null){
			for(TblInvoicestate curItem : invoicestates){				
				if(curItem.getIdInvoicestate() == curInvoicestate.getIdInvoicestate()){
					curItem.setSelected(true);
				}
			}
		}else{
			curInvoicestate = invoicestates.get(0);
		}		
	
		return invoicestates;
	}	
	
	public ArrayList<SelectItem> getInvoicestateListDescription(){
		ArrayList<SelectItem> invoicsestates = new ArrayList<SelectItem>();
		for (TblInvoicestate item : getInvoicestateList()) {
			invoicsestates.add(new SelectItem(item.getDescriptionIs()));
		}
		return invoicsestates;			
	}
	
	public void InvoicestateChange(ValueChangeEvent vce){
		setCurInvoicestate((TblInvoicestate) entityLister.getSingleObject("SELECT * FROM OMinvoicestate WHERE description_is = '" + 
				vce.getNewValue().toString() + "'", TblInvoicestate.class));				
	}

	public void setCurInvoicestate(TblInvoicestate curInvoicestate) {
		this.curInvoicestate = curInvoicestate;
	}

	public TblInvoicestate getCurInvoicestate() {
		return curInvoicestate;
	}
	
	ArrayList<TblIncometype> incometype;
	private TblIncometype curIncometype;
	
	@SuppressWarnings("unchecked")
	private ArrayList<TblIncometype> getIncometypeList(){
		/*invoicestates.clear();
		invoicestates.addAll(tblInvoicestate.getInvoicsestateList());
		return invoicestates;*/
		if(incometype == null){
			incometype = new ArrayList<TblIncometype>();
			incometype.addAll((Collection<? extends TblIncometype>) 
					entityLister.getObjectList("SELECT * FROM OMincometype", 
							TblIncometype.class));
		}
		
		if(curIncometype != null){
			for(TblIncometype curItem : incometype){				
				if(curItem.getIdIncometype() == curIncometype.getIdIncometype()){
					curItem.setSelected(true);
				}
			}
		}else{
			curIncometype = incometype.get(0);
		}		
	
		return incometype;
	}	
	
	public ArrayList<SelectItem> getIncometypeListDescription(){
		ArrayList<SelectItem> incometype = new ArrayList<SelectItem>();
		for (TblIncometype item : getIncometypeList()) {
			incometype.add(new SelectItem(item.getDescriptionIt()));
		}
		return incometype;			
	}
	
	public void IncometypeChange(ValueChangeEvent vce){
		setCurIncometype((TblIncometype) entityLister.getSingleObject("SELECT * FROM OMincometype WHERE description_it = '" + 
				vce.getNewValue().toString() + "'", TblIncometype.class));				
	}

	public void setCurIncometype(TblIncometype curIncometype) {
		this.curIncometype = curIncometype;
		curFilterInvoice.getTblSettlement().setTblIncometype(curIncometype);
	}

	public TblIncometype getCurIncometype() {
		return curIncometype;
	}
	
	ArrayList<TblOrder> order;
	private TblOrder curOrder;
	
	@SuppressWarnings("unchecked")
	private ArrayList<TblOrder> getOrderList(){
		/*invoicestates.clear();
		invoicestates.addAll(tblInvoicestate.getInvoicsestateList());
		return invoicestates;*/
		if(order == null){
			order = new ArrayList<TblOrder>();
			order.addAll((Collection<? extends TblOrder>) 
					entityLister.getObjectList("SELECT * FROM OMorder", 
							TblOrder.class));
		}
		
		if(curOrder != null){
			for(TblOrder curItem : order){				
				if(curItem.getIdOrder() == curOrder.getIdOrder()){
					curItem.setSelected(true);
				}
			}
		}else{
			curOrder = order.get(0);
		}		
	
		return order;
	}	
	
	public ArrayList<SelectItem> getOrderListDescription(){
		ArrayList<SelectItem> order = new ArrayList<SelectItem>();
		for (TblOrder item : getOrderList()) {
			order.add(new SelectItem(item.getOrdernumber()));
		}
		return order;			
	}
	
	public void OrderChange(ValueChangeEvent vce){
		setCurOrder((TblOrder) entityLister.getSingleObject("SELECT * FROM OMorder WHERE description_it = '" + 
				vce.getNewValue().toString() + "'", TblOrder.class));				
	}

	public void setCurOrder(TblOrder curOrder) {
		this.curOrder = curOrder;
		curFilterInvoice.getTblSettlement().setTblOrder(curOrder);
	}

	public TblOrder getCurOrder() {
		return curOrder;
	}
	
	ArrayList<TblSettlement> settlement;
	private TblSettlement curSettlement;
	
	@SuppressWarnings("unchecked")
	private ArrayList<TblSettlement> getSettlementList(){
		/*invoicestates.clear();
		invoicestates.addAll(tblInvoicestate.getInvoicsestateList());
		return invoicestates;*/
		if(settlement == null){
			settlement = new ArrayList<TblSettlement>();
			settlement.addAll((Collection<? extends TblSettlement>) 
					entityLister.getObjectList("SELECT * FROM OMsettlement", 
							TblSettlement.class));
		}
		
		if(curSettlement != null){
			for(TblSettlement curItem : settlement){				
				if(curItem.getIdSettlement() == curSettlement.getIdSettlement()){
					curItem.setSelected(true);
				}
			}
		}else{
			curSettlement = settlement.get(0);
		}		
	
		return settlement;
	}	
	
	public ArrayList<SelectItem> getSettlementListDescription(){
		ArrayList<SelectItem> settlement = new ArrayList<SelectItem>();
		for (TblSettlement item : getSettlementList()) {
			settlement.add(new SelectItem(item.getIdSettlement()));
		}
		return settlement;			
	}
	
	public void SettlementChange(ValueChangeEvent vce){
		setCurSettlement((TblSettlement) entityLister.getSingleObject("SELECT * FROM OMsettlement WHERE description_it = '" + 
				vce.getNewValue().toString() + "'", TblSettlement.class));				
	}

	public void setCurSettlement(TblSettlement curSettlement) {
		this.curSettlement = curSettlement;
		curFilterInvoice.setTblSettlement(curSettlement);
	}

	public TblSettlement getCurSettlement() {
		return curSettlement;
	}
	
	public TblInvoice curFilterInvoice;
	
	public TblInvoice getCurFilterInvoice(){
		return curFilterInvoice;
	}
	
	public void setCurFilterInvoice(TblInvoice invoice){
		this.curFilterInvoice = invoice;
	}
	
	public void DateChangeListener(ValueChangeEvent vce){
		getCurInvoice().setDuedate((Date) vce.getNewValue());
	}
	
	//___________________________________________________________________________
}